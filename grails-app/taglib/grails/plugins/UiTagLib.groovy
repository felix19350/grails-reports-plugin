package grails.plugins

class UiTagLib {

    static namespace = "ui"

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

}
