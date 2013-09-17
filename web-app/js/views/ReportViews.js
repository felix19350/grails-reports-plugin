jQuery(function () {

    AppViews.ReportView = BaseView.extend({

        el: $("#reportTemplateEditor"), //TODO: add the element

        deleteDialogTemplate: _.template($('#defaultDeleteDialogTpl').html()),

        deleteDialog: undefined,
        templateEditor: undefined,
        bindingEditor: undefined,
        reportMacros: undefined,


        events: {
            "click #previewBtn": "preview",
            "click #saveBtn": "save",
            "click #deleteBtn": "showDeleteDialog",
            "click .delete-btn-confirm": "delete",
            "click #undoBtn": "undo",
            "click #redoBtn": "redo"
        },

        initialize: function () {
            this.$el.append(this.deleteDialogTemplate({
                modalId: 'delete-dialog'
            }));
            this.deleteDialog = this.$("#delete-dialog");

            var self = this;
            $(window).resize(function () {
                self.resize();
            });

            this.bindFields(this.model.fields);
            this.render();
        },

        render: function () {
            var templateTextarea = this.$("textarea[name='templateDocument']");
            this.templateEditor = CodeMirror.fromTextArea(templateTextarea.get(0), {
                lineNumbers: true,
                matchBrackets: true,
                mode: "application/x-ejs",
                indentUnit: 4,
                indentWithTabs: true,
                enterMode: "keep",
                tabMode: "shift",
                lineWrapping: true
            });

            var bindingTextarea = this.$("textarea[name='bindingBuilder']");
            this.bindingEditor = CodeMirror.fromTextArea(bindingTextarea.get(0), {
                lineNumbers: true,
                matchBrackets: true,
                mode: "text/x-groovy",
                indentUnit: 4,
                indentWithTabs: true,
                enterMode: "keep",
                tabMode: "shift",
                lineWrapping: true
            });

            this.resize();
            return this;
        },

        undo: function () {
            this.templateEditor.undo();
        },

        redo: function () {
            this.templateEditor.redo();
        },

        preview: function () {

            var self = this;
            this.model.set({
                templateDocument: this.templateEditor.getValue(),
                bindingBuilder: this.bindingEditor.getValue()
            });

            this.model.setPreviewMode();
            this.model.save(undefined, {
                success: function () {
                    self.setExceptionStatus();
                    $("#iframe").get(0).contentDocument.location.reload(true);
                },
                error: function () {
                    log.error("An error has occured");
                }
            });

        },

        save: function () {

            this.model.setSaveMode();

            this.model.set({
                templateDocument: this.templateEditor.getValue(),
                bindingBuilder: this.bindingEditor.getValue()
            });

            this.model.save();
        },

        resize: function () {
            var span12Width = this.$(".span12").width();
            var windowWidth = $(window).width();

            if (windowWidth < (2 * span12Width)) {
                //Center the contents
                this.$("#ide-output").parent()
                    .removeClass("span11")
                    .addClass("span12");

                this.$el.parent().addClass("container");
                this.$("iframe").height(400);
            } else {
                //Put both panes side by side
                this.$("#ide-output").parent()
                    .removeClass("span12")
                    .addClass("span11");

                this.$el.parent().removeClass("container");
                this.$("iframe").height(800);
            }
        },

        setExceptionStatus: function () {
            var ex = this.model.get('exception');

            if (ex !== "" && !_.isNull(ex)) {
                this.showExceptions();
            } else {
                this.hideExceptions();
            }
        },

        hideExceptions: function () {
            this.$("#ide-output a[href='#ide-preview']").tab('show');
        },

        showExceptions: function () {

            this.$("#ide-output a[href='#ide-exception']").tab('show');
        },

        showDeleteDialog: function () {
            this.deleteDialog.modal('show');
        },

        "delete": function () {
            this.model.destroy({
                success: function () {
                    window.location.href = App.serverURL + "/reports";
                },
                error: function () {
                    log.error("An error has occurred deleting the report");
                }
            });

        }

    });

    AppViews.ReportListView = Backbone.View.extend({

        el: $("#reportListView"),
        reportTable: $("#reportTable"),
        createDialog: $("#create-report-dialog"),

        router: undefined,

        events: {
            "click #createReportBtn": "showCreateDialog",
            "click #saveReport": "save"
        },

        initialize: function () {
            this.collection.bind('add', this.addOne, this);
            this.collection.bind('reset', this.addAll, this);
            this.collection.bind('all', this.render, this);

            this._initRouter();
            this.collection.initSortableLinks(this.$el.find("table:first"), this.router);
            this.addAll();
        },

        addOne: function (report) {
            var view = new AppViews.ReportListItemView({
                model: report
            });
            this.reportTable.append(view.render().el);
        },

        addAll: function () {
            this.reportTable.empty();
            if (this.collection.size() === 0) {
                this._displayEmptyMsg();
            } else {
                this.collection.each(function (report) {
                    this.addOne(report);
                }, this);
            }
        },

        save: function () {
            var title = this.createDialog.find("input[name='title']").val();
            var name = this.createDialog.find("input[name='name']").val();
            var model = new AppModels.Report({
                title: title,
                name: name
            });

            var self = this;
            model.save(undefined, {
                success: function () {
                    self.createDialog.modal('hide');
                    self.addOne(model);
                },
                error: function () {
                    log.error("An error has occured");
                }
            });
        },

        showCreateDialog: function () {
            this.createDialog.modal('show');
        },

        _initRouter: function () {
            this.router = new Backbone.Router();

            //Dynamically add the routes based on the functionality of the collection
            this.collection.setupRoutes(this.router);

            Backbone.history.start();
        },


        _displayEmptyMsg: function () {
            //AppTemplates.showEmptyTableMsg(this.reportTable);
        }

    });


    AppViews.ReportListItemView = Backbone.View.extend({
        tagName: "tr",

        template: _.template($('#reportListItemTpl').html()),

        events: {
            "click": "show",
            "click .download" : "downloadReport"
        },

        initialize: function () {
            this.model.bind('change', this.render, this);
        },

        show: function (e) {
            e.preventDefault();
            window.location = this.model.url();
        },
        downloadReport: function(e) {
            e.stopPropagation();
            var url = App.serverURL + "/report/download/" + this.model.get("id");
            window.location = url;
        },
        render: function () {
            var data = this.model.toJSON();
            this.$el.html(this.template(data));
            this.$el.addClass("clickable");
            return this;
        }
    });

});