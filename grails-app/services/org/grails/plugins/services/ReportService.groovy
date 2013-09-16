package org.grails.plugins.services

import org.grails.plugins.reports.NotFoundException
import grails.util.GrailsUtil
import grails.util.GrailsWebUtil
import groovy.text.Template
import org.codehaus.groovy.control.CompilationFailedException
import org.codehaus.groovy.control.CompilerConfiguration
import org.codehaus.groovy.grails.compiler.web.pages.GroovyPageClassLoader
import org.codehaus.groovy.grails.exceptions.DefaultErrorsPrinter
import org.codehaus.groovy.grails.web.errors.GrailsExceptionResolver
import org.codehaus.groovy.grails.web.pages.GroovyPageMetaInfo
import org.codehaus.groovy.grails.web.pages.GroovyPageParser
import org.codehaus.groovy.grails.web.pages.GroovyPageTemplate
import org.codehaus.groovy.grails.web.pages.GroovyPagesMetaUtils
import org.codehaus.groovy.grails.web.pages.exceptions.GroovyPagesException
import org.codehaus.groovy.grails.web.servlet.WrappedResponseHolder
import org.codehaus.groovy.runtime.IOGroovyMethods
import org.grails.plugins.reports.Report
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.servlet.DispatcherServlet
import org.springframework.web.servlet.i18n.FixedLocaleResolver
import org.w3c.dom.Document
import org.xhtmlrenderer.resource.XMLResource
import org.xml.sax.InputSource

import javax.servlet.http.HttpServletResponse
import java.util.concurrent.ConcurrentHashMap

class ReportService {

    def pdfRenderingService
    def grailsApplication

    def jspTagLibraryResolver
    def gspTagLibraryLookup
    def myClassLoader

    private Map<String, GroovyPageMetaInfo> pageCache = new ConcurrentHashMap<String, GroovyPageMetaInfo>();

    boolean useCache = true
    boolean cleanClassLoaderCache = false

    static transactional = true

    /**
     * Renders the report to one http response that can be to see in browser (isInline = true) or to download (isInline = false)
     * the filename will be report.title + ' - ' + filesubname + '.pdf'
     *
     * @param name
     * @param binding
     * @param response
     * @param filesubname
     * @param isInline
     *
     */
    public void renderReport(String name, Map binding, HttpServletResponse response, String filesubname = null, boolean isInline = true) {
        log.debug("Render report '${name}' to http response")
        def reportInstance = Report.findByName(name)

        if (!reportInstance) {
            throw new NotFoundException(name, Report)
        }

        renderReport(reportInstance, binding, response, filesubname, isInline)
    }

    /**
     * Renders the report and returns the PDF bytes
     * @param name
     * @param binding
     *
     */
    public byte[] renderReport(String name, Map binding) {
        log.debug("Render report '${name}'")
        def reportInstance = Report.findByName(name)

        if (!reportInstance) {
            throw new NotFoundException(name, Report)
        }

        def templateDocument = reportInstance.templateDocument

        String xhtml = evalReportTemplate(templateDocument, binding)
        Document document = createDocument(xhtml)
        OutputStream out = new ByteArrayOutputStream()
        pdfRenderingService.render([document: document], out)
        return out.toByteArray()
    }

    // gets template source and filename
    public void renderReport(Report reportInstance, Map binding, HttpServletResponse response, String filesubname, boolean isInline) {
        def templateDocument = reportInstance.templateDocument
        def filename = reportInstance.title

        if (filesubname != null) {
            filename += " - " + filesubname
        }
        filename += ".pdf"
        log.debug("Filename is '${filename}'")
        renderReportTemplate(templateDocument, binding, response, filename, isInline)
    }

    // write content disposition
    private void renderReportTemplate(String templateDocument, Map binding, HttpServletResponse response, String filename, boolean isInline) {
        log.debug("renderReportTemplate: ${filename}")
        def contentDisposition = isInline ? "inline" : "attachment"
        log.debug("Content disposition is ${contentDisposition}")
        response.setContentType("application/pdf")
        response.setHeader("Content-Disposition", "${contentDisposition}; filename=${filename}")

        renderTemplateDocument(templateDocument, binding, response.outputStream)
    }

    /**
     * Render template document (gsp source) to output stream (in pdf)
     */
    private void renderTemplateDocument(String templateDocument, Map binding, OutputStream os) {
        //log.debug("Got to evalReportTemplate")
        String xhtml = evalReportTemplate(templateDocument, binding)
        //log.debug("Got to createDocument")
        Document document = createDocument(xhtml)
        //log.debug("Got to render pdf")
        pdfRenderingService.render([document: document], os)
        //log.debug("Done")
    }

    /**
     * Create Dom object from xhtml code
     */
    private Document createDocument(String xhtml) {
        InputSource xhtmlInput = new InputSource(new StringReader(xhtml))
        return XMLResource.load(xhtmlInput).document
    }

    /**
     * Evaluate template text (gsp) with given bindings
     *
     * (the name used to compile gsp is base on the md5 of its content)
     */
    private String evalReportTemplate(String templateText, Map data) {
        def xhtmlWriter = new StringWriter()
        def name = "_report_" + templateText.encodeAsMD5()
        def template = createTemplate(templateText, name)

        // replace the request - copy from MailMessageContentRenderer
        def applicationContext = grailsApplication.mainContext
        def originalRequestAttributes = RequestContextHolder.getRequestAttributes()
        def renderRequestAttributes = GrailsWebUtil.bindMockWebRequest(applicationContext)

        if (originalRequestAttributes) {
            renderRequestAttributes.controllerName = originalRequestAttributes.controllerName
        }

        renderRequestAttributes.request.setAttribute(DispatcherServlet.LOCALE_RESOLVER_ATTRIBUTE, new FixedLocaleResolver(defaultLocale: new Locale("pt", "PT")))

        renderRequestAttributes.setOut(xhtmlWriter)
        WrappedResponseHolder.wrappedResponse = renderRequestAttributes.currentResponse

        template.make(data).writeTo(xhtmlWriter)

        RequestContextHolder.setRequestAttributes(originalRequestAttributes) // null ok
        WrappedResponseHolder.wrappedResponse = originalRequestAttributes?.currentResponse



        return xhtmlWriter.toString()
    }

    // "copy" of GroovyPagesTemplateEngine

    private Template createTemplate(String txt, String pageName) throws IOException {

        if (useCache) {

            GroovyPageMetaInfo metaInfo = pageCache.get(pageName)

            if (metaInfo != null) {
                log.debug("Gsp page in cache [${pageName}]")
                return new GroovyPageTemplate(metaInfo);
            } else {
                log.debug("Gsp page not in cache [${pageName}]")
                return createTemplate(new ByteArrayResource(txt.getBytes("UTF-8"), pageName), pageName);
            }

        } else {

            return createTemplate(new ByteArrayResource(txt.getBytes("UTF-8"), pageName), pageName);

        }
    }

    public void clearPageCache() {
        pageCache.clear();
    }

    private Template createTemplate(Resource resource, String pageName) throws IOException {
        InputStream ins = resource.getInputStream();
        try {
            return createTemplate(ins, resource, pageName);
        }
        finally {
            ins.close();
        }
    }

    private Template createTemplate(InputStream inputStream, Resource resource, String pageName) {
        GroovyPageMetaInfo metaInfo = buildPageMetaInfo(inputStream, resource, pageName);
        if (useCache) {
            pageCache.put(pageName, metaInfo);
        }
        return new GroovyPageTemplate(metaInfo);
    }


    private Template createTemplate(InputStream inputStream, String pageName) {
        GroovyPageMetaInfo metaInfo = buildPageMetaInfo(inputStream, null, pageName);
        return new GroovyPageTemplate(metaInfo);
    }

    private GroovyPageMetaInfo buildPageMetaInfo(InputStream inputStream, Resource res, String pageName) {
        String name = pageName;

        GroovyPageParser parser;
        String path = res.getDescription();
        try {
            String encoding = GroovyPageParser.DEFAULT_ENCODING;
            if (grailsApplication != null) {
                Map<String, Object> config = grailsApplication.getFlatConfig();
                Object gspEnc = config.get(GroovyPageParser.CONFIG_PROPERTY_GSP_ENCODING);
                if ((gspEnc != null) && (gspEnc.toString().trim().length() > 0)) {
                    encoding = gspEnc.toString();
                }

            }
            parser = new GroovyPageParser(name, path, path, inputStream, encoding);

        }
        catch (IOException e) {
            throw new GroovyPagesException("I/O parsing Groovy page [" + (res != null ? res.getDescription() : name) + "]: " + e.getMessage(), e);
        }

        InputStream ins = parser.parse();

        // Make a new metaInfo
        GroovyPageMetaInfo metaInfo = createPageMetaInfo(parser, ins);
        metaInfo.applyLastModifiedFromResource(res);
        try {
            metaInfo.setPageClass(compileGroovyPage(ins, name, path, metaInfo));
            metaInfo.setHtmlParts(parser.getHtmlPartsArray());
        }
        catch (GroovyPagesException e) {
            metaInfo.setCompilationException(e);
        }

        return metaInfo;
    }

    private GroovyPageMetaInfo createPageMetaInfo(GroovyPageParser parse, InputStream ins) {
        GroovyPageMetaInfo pageMeta = new GroovyPageMetaInfo();
        pageMeta.setGrailsApplication(grailsApplication);
        pageMeta.setJspTagLibraryResolver(jspTagLibraryResolver);
        pageMeta.setTagLibraryLookup(gspTagLibraryLookup);
        pageMeta.setContentType(parse.getContentType());
        pageMeta.setLineNumbers(parse.getLineNumberMatrix());
        pageMeta.setJspTags(parse.getJspTags());
        pageMeta.setCodecName(parse.getDefaultCodecDirectiveValue());
        pageMeta.initialize();
        // just return groovy and don't compile if asked
        if (GrailsUtil.isDevelopmentEnv()) {
            pageMeta.setGroovySource(ins);
        }

        return pageMeta;
    }

    private Class<?> compileGroovyPage(InputStream ins, String name, String pageName, GroovyPageMetaInfo metaInfo) {
        GroovyClassLoader groovyClassLoader = findOrInitGroovyClassLoader();

        // Compile the script into an object
        Class<?> scriptClass;
        try {
            String groovySource = IOGroovyMethods.getText(ins, GroovyPageParser.GROOVY_SOURCE_CHAR_ENCODING);
            //System.out.println(groovySource);
            //log.debug("classes loaded ${groovyClassLoader.getLoadedClasses().size()}")
            scriptClass = groovyClassLoader.parseClass(groovySource, name);
            //log.debug("classes loaded ${groovyClassLoader.getLoadedClasses().size()}")
        }
        catch (CompilationFailedException e) {
            log.error("Compilation error compiling GSP [" + name + "]:" + e.getMessage(), e);

            int lineNumber = GrailsExceptionResolver.extractLineNumber(e);

            final int[] lineMappings = metaInfo.getLineNumbers();
            if (lineNumber > 0 && lineNumber < lineMappings.length) {
                lineNumber = lineMappings[lineNumber - 1];
            }
            String relativePageName = DefaultErrorsPrinter.makeRelativeIfPossible(pageName);
            throw new GroovyPagesException("Could not parse script [" + relativePageName + "]: " + e.getMessage(), e, lineNumber, pageName);
        }
        catch (IOException e) {
            String relativePageName = DefaultErrorsPrinter.makeRelativeIfPossible(pageName);
            throw new GroovyPagesException("IO exception parsing script [" + relativePageName + "]: " + e.getMessage(), e);
        }
        GroovyPagesMetaUtils.registerMethodMissingForGSP(scriptClass, gspTagLibraryLookup);

        if (cleanClassLoaderCache) {
            groovyClassLoader.clearCache()
        }

        return scriptClass;
    }




    private synchronized GroovyClassLoader findOrInitGroovyClassLoader() {
        if (myClassLoader == null) {
            myClassLoader = initGroovyClassLoader(Thread.currentThread().getContextClassLoader());
        }
        return (GroovyClassLoader) myClassLoader;
    }


    private GroovyClassLoader initGroovyClassLoader(ClassLoader parent) {
        CompilerConfiguration compConfig = new CompilerConfiguration();
        compConfig.setSourceEncoding(GroovyPageParser.GROOVY_SOURCE_CHAR_ENCODING);
        return new GroovyPageClassLoader(parent, compConfig);
    }

}