package grails.plugins

class UiTagLib {

    static namespace = "ui"

    def grailsApplication

    /**
     * Generates the clinic/company logo
     * */
    def companyLogo = {
        attrs, src ->
            out << """
		<div class="pull-left" style="margin-top: 6px;">
		<img src="${g.resource(dir: 'images', file: grailsApplication.config.organizationLogo)}" alt="" style="height: 34px;" />
		</div>
		<div class="pull-left" style="font-size: 10px; margin-right: 20px; margin-top: 6px;">
		<p style="color: #8870B1; border-bottom: 1px solid #999; margin: 0;">${grailsApplication.config.organizationName}</p>
		<p class="bold uppercase" style="color: #999; margin: 0; font-size: 10px; text-align: center;">${grailsApplication.config.organizationalUnitName}</p>
		</div>
	  """
    }

    /**
     * Creates a sortable column header that works with the Backbone routing scheme implemented.
     * Creates a TH element that contains a link with the an hash of the property to be sorted.
     * @attr property - Name of the property to be sorted
     * @attr label - Label for the property. It is passed on to the message source
     * @attr defaultOrder - optional parameter, can be: "asc" or "desc"
     */
    def sortableColumn = {
        attrs, body ->
            def property = attrs.property;
            if (!property) {
                throwTagError("Tag [sortableColumn] requires attribute [property]")
            }

            def colspan = attrs.colspan ?: 1
            def label = attrs.label ? g.message(code: attrs.label) : property
            def order = (attrs?.defaultOrder == "asc" || attrs?.defaultOrder == "desc") ? attrs.defaultOrder : "asc"
            def defaultClass = "ui-sort"

            out << """<th colspan="${colspan}" class="${order}"><a class="${defaultClass}" href="#s/${property}/${order}">${label}</a></th>"""
    }

    /**
     Requires twitter bootstrap for the display to be correct.
     name: name to show
     editName: name to edit - default = name
     label: title
     type: text, textarea, date, number, radio, checkbox, autocomplete
     options: map used when type is select, radio or checkbox
     editable: true|false - default false
     viewable: true|false - default true
     optionKey
     optionValue
     */
    def property = {
        attrs, body ->

            if (!attrs.containsKey('name')) {
                throwTagError("Tag [property] is missing required attribute [name]")
            }

            def label = attrs.label
            def name = attrs.name

            def required = parseBoolean(attrs.required, false)
            def editable = parseBoolean(attrs.editable, false)
            def viewable = parseBoolean(attrs.viewable, true)

            def editName = attrs.editName ?: name
            // INFO: é util gerar um random para alguma coisa?
            //def editId = attrs.editId ?: UUID.randomUUID().toString()
            def type = attrs.type ?: "text"
            def placeholder = attrs.placeholder ?: ""

            def rowClass = attrs.rowClass ?: "row"          //Class for the row
            def labelClass = attrs.labelClass ?: "span2"    //Class for the label of the field

            def valueClass = attrs.valueClass ?: "span2"    //Class for the value of the field

            def inputClass = attrs.inputClass ?: "span2"   //Class for the input field
            def inputContainerClass = attrs.inputContainerClass ?: inputClass //Class for the container of the input field

            out << """<div class="${rowClass}">"""
            if (label != null) {
                if (viewable && !editable) {
                    out << """<div class="${labelClass} display uniform-height">${label}</div>"""
                } else if (!viewable && editable) {
                    out << """<div class="${labelClass} edit uniform-height">${label}</div>"""
                } else if (viewable && editable) {
                    out << """<div class="${labelClass} uniform-height">${label}</div>"""
                }
            }


            if (viewable) {
                out << """<div class="display uniform-height ${valueClass}">"""
                switch (type) {
                    case "checkbox":
                        out << """<input disabled="disabled" data-synapse-interface="checked" name="${name}" type="checkbox"/>"""
                        break
                    default:
                        out << """<span data-bind="${name}"></span>"""
                }
                out << """</div>"""

            }

            if (editable) {
                out << """<div class="edit ${inputContainerClass}">"""

                switch (type) {
                    case "select":
                        def options = attrs.options ?: []

                        def optionKey = attrs.optionKey
                        def optionValue = attrs.optionValue
                        def valueMessagePrefix = attrs.valueMessagePrefix
                        def noSelection = attrs.noSelection

                        out << g.select(class: "${inputClass} ${required ? 'required' : '' }", name: editName, from: options, optionKey: optionKey, optionValue: optionValue, valueMessagePrefix: valueMessagePrefix, noSelection: noSelection)
                        break

                    case "radio":
                        def values = attrs.values ?: []
                        def labels = attrs.labels ?: []
                        out << g.radioGroup(name: editName, values: values, labels: labels)
                        break

                    case "checkbox":
                        out << """<input name="${editName}" class="${required ? 'required' : '' }" data-synapse-interface="checked" type="checkbox"/>"""
                        break

                    case "date":
                        out << ui.dateField([name: editName, placeholder: placeholder, required: required])
                        break

                    case "datePicker":
                        out << ui.datePicker([inputClass: inputClass, name: editName, required: required, date: attrs.date])
                        break

                    case "timePicker":
                        out << ui.timePicker([inputClass: inputClass, name: editName])
                        break

                    case "country":
                        def valueMessagePrefix = attrs.valueMessagePrefix
                        out << g.countrySelect(name: editName, valueMessagePrefix: valueMessagePrefix)
                        break

                    case "email":
                        out << """<input class="${inputClass} ${required ? 'required' : '' }" name="${editName}" type="email" placeholder="${placeholder}"/>"""
                        break

                    case "number":
                        //out << """<input class="${inputClass}" name="${editName}" type="number"/>""" IE Sucks :-(
                        out << """<input class="${inputClass} ${required ? 'required' : '' }" name="${editName}" type="text" placeholder="${placeholder}"/>"""
                        break

                    case "latitude":
                        //out << """<input class="${inputClass}" name="${editName}" type="text" min="-90.0" max="90.0" />"""
                        out << """<input class="${inputClass} ${required ? 'required' : '' }" name="${editName}" type="text" placeholder="${placeholder}"/>"""
                        break

                    case "longitude":
                        //out << """<input class="${inputClass}" name="${editName}" type="text" min="-180.0" max="180.0" />"""
                        out << """<input class="${inputClass} ${required ? 'required' : '' }" name="${editName}" type="text" placeholder="${placeholder}"/>"""
                        break

                    case "textarea":
                        out << """<textarea class="${inputClass} ${required ? 'required' : '' }" name="${editName}" placeholder="${placeholder}"></textarea>"""
                        break

                    case "password":
                        out << """<input class="${inputClass}" name="${editName}" type="password"/>"""
                        break

                    case "autocomplete":
                        out << ui.autoComplete(options: attrs.options, editName: editName, inputClass: inputClass)
                        break

                    default:
                        out << """<input class="${inputClass} ${required ? 'required' : '' }" name="${editName}" type="text" placeholder="${placeholder}"/>"""
                }

                out << """</div>"""
            }
            out << """</div>"""

    }

    def autoComplete = { attrs, body ->
        def options = attrs?.options?.collect { '"' + it.toString() + '"' }.join(',') ?: []
        def data = options.size() > 0 ? "[${options}]" : "[]"
        out << """<input class="${attrs.inputClass}" autocomplete="off" name="${attrs.editName}" type="text" data-provide="typeahead" data-items="8" data-source='${data}'>"""
    }


    def dateField = { attrs, body ->
        def name = attrs.name
        def placeholder = attrs.placeholder ?: g.message(code: 'ui.label.dateFormat')
        def required = attrs.required ?: false
        def value = attrs.value ?: ""
        def extraClasses = attrs.classes ?: ""
        def inputClasses = attrs.inputClasses ?: "span2"
        out << """<div class="date-picker-holder ${extraClasses}">
              <input class="${inputClasses} ${required ? 'required' : ''}" name="${name}" type="text" placeholder="${placeholder}" value="${value}"/>
              </div>"""
    }


    def datePickerInline = { attrs, body ->
        def name = attrs.name
        def format = attrs.format ?: "yyyy-mm-dd"
        def date = attrs.date ?: (new Date()).encodeAsDefaultDate()
        def containerClass = attrs.containerClass ?: ""

        out << """
			<div id="${name}" name="${name}" class="date ${containerClass}" data-date-format="${format}" data-date="${date}"></div>
		"""
    }


    def datePicker = { attrs, body ->
        def name = attrs.name
        def format = attrs.format ?: "yyyy-mm-dd"
        def required = attrs.required ?: false
        def date = attrs.date ?: (new Date()).encodeAsDefaultDate()
        def inputClass = attrs.inputClass ?: ""

        out << """
			<div class="input-append date" data-date-format="${format}" data-date="${date}" data-date-language="pt" >
				<input class="${inputClass}" name="${name}" type="text" readonly="readonly" class="${required ? 'required' : ''}" value="${date}">
				<span class="add-on" style="margin-left: -5px;">
					<i class="icon-calendar"></i>
				</span>
			</div>
			
		"""
    }

    def timePicker = { attrs, body ->
        def name = attrs.name
        def time = attrs.time ?: (new Date()).encodeAsDefaultTime()
        def inputClass = attrs.inputClass ?: ""

        out << """
			<div class="input-append bootstrap-timepicker-component">
				<input name="${name}" class="${inputClass} timepicker" type="text" value="${time}" readonly="readonly"/>
				<span class="add-on" style="margin-left: -5px;">
					<i class="icon-time"></i>
				</span>
			</div>
		"""
    }

    def actionButtonFrame = { attrs, body ->
        def canEdit = parseBoolean(attrs.canEdit)
        def canDelete = parseBoolean(attrs.canDelete)

        def toolbarContainerClass = attrs.toolbarContainerClass ?: "action-toolbar-container"
        def toolbarClass = attrs.toolbarClass ?: "action-toolbar"
        def contentClass = attrs.contentClass ?: "row"

        def withSeparator = parseBoolean(attrs.withHr, true)


        out << """<div class="${toolbarContainerClass} clearfix"> """


        if (canEdit) {
            out << """
          <div class="display ${toolbarClass}">
                <div class="pull-right"><span class="label edit-btn" style="margin-bottom: 5px;"><i class="icon-pencil"></i> ${message(code: 'ui.button.edit')}</span></div>
          </div>
          <div class="edit ${toolbarClass}">
              <div class="pull-right"><span class="label label-success save-btn" style="margin-bottom: 5px;"><i class="icon-ok"></i> ${message(code: 'ui.button.save')}</span></div>
          </div>
      """
        }

        if (attrs.title) {
            out << """
      <div class="pull-left clearfix">
        <h4 class="title">${attrs.title}</h4>
      </div>
      """
        }

        out << """</div>""" //End top clearfix

        if (withSeparator) {
            out << """<hr class="small-margin-top"/>"""
        }


        out << """<div class="cleared ${contentClass}">"""
        out << body()
        out << "</div>"

        if (canEdit) {
            out << """
          <div class="edit bottom-action-toolbar small-margin-top small-margin-bottom">
          <div class="row">
            <div class="pull-right"><span class="label cancel-btn"><i class="icon-remove"></i> ${message(code: 'ui.button.cancel')}</span></div>"""

            if (canDelete) {
                def modalId = UUID.randomUUID().toString()
                out << """<div class="pull-left">
                    <span class="label label-important delete-btn" data-toggle="modal" data-target="#${modalId}"><i class="icon-trash"></i> ${message(code: 'ui.button.delete')}</span>
                    <div class="modal hide fade" id="${modalId}">
                    <div class="modal-header">
                    <a href="#" class="close" data-dismiss="modal">&times;</a>
                    <h3>${g.message(code: 'ui.label.deleteConfirm')}</h3>
                    </div>
                    <div class="modal-body">
                    <p>${g.message(code: 'ui.label.deleteConfirmBody')}</p>
                    </div>
                    <div class="modal-footer">
                    <a href="#" class="btn btn-danger delete-btn-confirm">${g.message(code: 'ui.button.delete')}</a>
                    </div>
                    </div>
                  </div>"""
            }

            out << """</div>
          </div>
          """
        }

        //out << """</div>"""

    }

    def inlineRemoveBtn = { attrs, body ->
        def buttonClass = attrs.btnClass ?: ""
        out << """<div class="edit"><div class="${buttonClass} clickable" rel="tooltip" title="${g.message(code: 'ui.button.delete')}"> <i class="icon-minus-sign"></i></div></div>"""
    }

    def inlineEditBtn = { attrs, body ->
        def buttonClass = attrs.btnClass ?: ""
        out << """<div class="edit"><div class="${buttonClass} clickable" rel="tooltip" title="${g.message(code: 'ui.button.edit')}"> <i class="icon-ok-sign"></i></div></div>"""
    }

    def inlineCancelBtn = { attrs, body ->
        def buttonClass = attrs.btnClass ?: ""
        out << """<div class="edit"><div class="${buttonClass} clickable" rel="tooltip" title="${g.message(code: 'ui.button.cancel')}"> <i class="icon-remove"></i></div></div>"""
    }

    private boolean parseBoolean(attr, nullValue = false) {
        boolean result = nullValue
        if (attr == null) {
            result = nullValue
        } else if (attr instanceof Boolean) {
            result = attr
        } else {
            try {
                result = Boolean.parseBoolean(attr)
            } catch (Exception) {
                log.error("Error parsing boolean")
            }
        }

        return result
    }

    def entityTitle = { attrs, body ->
        def divClass = attrs.divClass ?: "span8"
        out << """
      <div class="pull-left">
        <div class="${divClass}">
        """

        if (attrs.icon) {
            out << """
        <img src="${attrs.icon}" alt=""/>
      """
        }

        out << """
          <h3 class="title inline">${attrs.message}</h3>
        </div>
      </div>
    """
    }

    def secondLevelTitle = { attrs, body ->
        if (!attrs.steps) {
            throwTagError "The tag [secondLevelTitle] is missing the property [steps]. This property should be an array with the following strcuture: [ [name, link], [name2, link2] ...]"
        }

        out << """<div class="pull-left">"""

        attrs?.steps?.eachWithIndex { step, idx ->
            if (idx == 0) {
                out << """<h3 class="inline default-margin-right">${step[0]}</h3>"""
            } else {
                out << """<h6 class="inline default-margin-right">${step[0]}</h6>"""
            }

        }

        out << """</div>"""
    }

    def toggleEditBtn = { attrs, body ->
        def buttonClass = attrs.btnClass ?: ""
        out << """
      <div class="display ${buttonClass} clickable" rel="tooltip" title="${g.message(code: 'ui.button.edit')}">
      <i class="icon-pencil"></i>
      </div>
    """
    }


}
