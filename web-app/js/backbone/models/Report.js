jQuery(function(){

	AppModels.Report = Backbone.Model.extend({
		
		urlRoot: App.serverURL + "/report",	//TODO: change the URL root to something valid
		
		mode: undefined,

		fields: [
		"title",
		"hook",
		"templateDocument",
		"sampleBinding",
		"output",
		"exception"
		],
		defaults: {
			"hook":				"",
            "title":            "",
			"templateDocument": "",
            "sampleBinding":    "",
            "output":           "",
            "exception":        ""
		}, 
		
		validation: { },

		setSaveMode: function(){
			this.mode = undefined;
		},

		setPreviewMode: function(){
			this.mode = "setPreviewParams";
		},

		url: function(){

			var sufix = "";
			if(!this.isNew()){
				sufix += "/" + this.get('id');
			}

			if(!_.isUndefined(this.mode)){
				sufix += "/" + this.mode;
			}

			return this.urlRoot + sufix;
		}
	});

	

	AppModels.ReportCollection = Backbone.Collection.extend(
		_.extend({},  SortableCollection, PaginatedCollection, {
		
			baseUrl: App.serverURL + "/reports",
			model: AppModels.Report,


			applySort:function (sortParam, sortOrder) {
				this.resetPaging();
				return SortableCollection.applySort.call(this, sortParam, sortOrder);
			},


			url:function () {
				//Default parameters to provide paging
				var urlParams = {
					page:this.page, 
					perPage:this.perPage
				};

				//Apply sort parameters if any
				if (!_.isUndefined(this._sortParams.param)) {
					urlParams.sort = this._sortParams.param;
					urlParams.order = this._sortParams.order;
				}


				return this.baseUrl + '?' + $.param(urlParams);
			},

			setupRoutes: function(router){
				PaginatedCollection.addRoutes(router, this);    //Routes & callbacks for pagination
				SortableCollection.addRoutes(router, this);     //Routes & callbacks for sorting
			}

		})
		);

});
