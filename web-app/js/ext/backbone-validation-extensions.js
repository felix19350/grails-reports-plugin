/**
 * Extends the backbone validations to provide default methods to handle valid and invalid states
 */

/*_.extend(Backbone.Validation.callbacks, {
    valid:function (view, attr, selector) {
        var nodes = view.$('[' + selector + '~=' + attr + ']');

        nodes.next('.help-inline').remove();
        nodes.closest('.row').removeClass('error');

        //log.debug("Attr " + attr + " is valid");
    },

    invalid:function (view, attr, error, selector) {
        var nodes = view.$('[' + selector + '~=' + attr + ']');

        if (nodes.next('.help-inline').length === 0) {
            nodes.after('<div class="help-inline">' + error + '</div>');
        }

        nodes.closest('.row').addClass('error');

        //log.debug("Attr " + attr + " is invalid");
    }
});*/

/*_.extend(Backbone.Validation.patterns, {
    floatNumber: /^-?(?:\d+)(?:\.\d+)?$/,
    defaultDate: /^\d{4}-(0[1-9]|1[0-2])-(0[1-9]|1[0-9]|2[0-9]|3[0-1])$/ //RFC 3339 date
});*/

/*
_.extend(Backbone.Validation.messages, {
    required:   messages.js.validation.required(),
    min:        messages.js.validation.min(),
    max:        messages.js.validation.max(),
    length:     messages.js.validation.length(),
    minLength:  messages.js.validation.minLength(),
    maxLength:  messages.js.validation.maxLength(),
    pattern:    messages.js.validation.pattern()
});
*/
