/**
 * Basic view from which other views that require data-binding via synapse should extend
 * */
BaseView = Backbone.View.extend({

    /**
     * Binds the elements in the UI to a backbone model.
     * @param attributes - attributes of the model that require binding
     * @param target - (optional) the target Jquery DOM node that serves as root to all the selectors used. Defaults to this.el
     * @param model - (optional) the model that will be bound to the UI elements. Defaults to this.model
     */
    bindFields: function (attributes, target, model) {

        if (_.isUndefined(target)) {
            target = $(this.el);
        }

        var targetModel = this.model;
        if (!_.isUndefined(model)) {
            targetModel = model;
        }

        if (_.isUndefined(attributes) && _.isArray(this.model.fields)) {
            attributes = this.model.fields;
        }

        var modelSynapse = new Synapse(targetModel);

        _.each(attributes, function (attr) {
            var input = target.find('[name=' + attr + ']');
            var field = target.find('[data-bind=' + attr + ']');

            input.each(function () {
                log.trace("Creating synapse for input " + attr);
                var inputNode = $(this);
                var inputSynapse = new Synapse(this);
                modelSynapse.notify(inputSynapse);
                modelSynapse.observe(inputSynapse);

                inputNode.on("focus", function () {
                    //log.debug("Focus event");
                    modelSynapse.pauseNotifying(inputSynapse);
                });

                inputNode.on("blur", function () {
                    //log.debug("Blur event");
                    modelSynapse.resumeNotifying(inputSynapse);
                });
            });

            field.each(function () {
                log.trace("Creating synapse for field " + attr);
                var fieldNode = $(this);
                var fieldSynapse = new Synapse(this);
                var fieldSynapseInterface = fieldNode.data("synapse-interface");
                if (_.isUndefined(fieldSynapseInterface)) {
                    modelSynapse.notify(fieldSynapse);
                } else {
                    var options = {
                        observerInterface: fieldSynapseInterface
                    };
                    modelSynapse.notify(fieldSynapse, options);
                }
            });

        });
    },

    unbindFields: function (attributes, target, model) {

        if (_.isUndefined(target)) {
            target = $(this.el);
        }

        var targetModel = this.model;
        if (!_.isUndefined(model)) {
            targetModel = model;
        }

        var modelSynapse = new Synapse(targetModel);
        _.each(attributes, function (attr) {
            var input = target.find('[name=' + attr + ']');
            var field = target.find('[data-bind=' + attr + ']');

            input.each(function () {
                log.trace("Unbinding synapse for input " + attr);
                var inputNode = $(this);
                var inputSynapse = new Synapse(this);
                inputNode.off("blur");
                inputNode.off("focus");
                try {
                    modelSynapse.stopNotifying(inputSynapse);
                    modelSynapse.stopObserving(inputSynapse);
                } catch (ignore) {
                    log.error("Error in unbindFields for input " + attr);
                    //Do nothing on purpose
                }

            });

            field.each(function () {
                log.trace("Unbinding synapse for field " + attr);
                var fieldNode = $(this);
                var fieldSynapse = new Synapse(this);
                try {
                    modelSynapse.stopNotifying(fieldSynapse);
                } catch (ignore) {
                    log.error("Error in unbindFields for field " + attr);
                }

            });

        });

    }

});