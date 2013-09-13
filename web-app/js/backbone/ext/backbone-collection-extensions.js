/**
 * Allows a backbone collection to be filterable
 */
FilterableCollection = {

    filters:{},
    filterParser: undefined,    //Method used to parse the filter settings.

    defaultFilterParser: function(filterExpression){
        var result = {};
        var tokens = filterExpression.split("/");
        var i;
        
        for(i in tokens){
            var subTokens = tokens[i].split("=");
            if(subTokens.length == 2){
                result[subTokens[0]] = subTokens[1];
            }
        }

        return result;
    },

    addFilterComponent: function(field, value){
        this.filters[field] = value;
    },

    removeFilterComponents: function(field){
        delete this.filters[field];
    },

    resetFilters: function(){
        this.filters = {};
    },

    /**
     * Method that parses a generic filter string using the filterParser property.
     * */
    applyGenericDataFilter: function(filters){
        if(!_.isUndefined(this.filterParser)){
            this.filters = this.filterParser.call(this, filters);
        }else{
            this.filters = this.defaultFilterParser(filters);
        }

        return this.fetch();
    },

    applyDataFilter: function(field, value){
        this.addFilterComponent(field, value);
        return this.fetch();
    },

    /**
     * Dynamically adds a route that allows a collection to be filtered.
     * @param router
     * @param ctx
     */
    addRoutes: function(router, ctx){
        if(_.isUndefined(ctx)){
            ctx = this;
        }

        router.route("f/:field/:value", "filter", function(field, value){
            ctx.applyDataFilter(field, value);
        });

        router.route("f/*filters", "filter", function(filters){
            ctx.applyGenericDataFilter(filters);
        });
    }
};

/**
 * Allows a backbone collection to become sortable.
 */
SortableCollection = {

    scOrders:{
        asc: "asc",
        desc: "desc"
    },

    _sortParams:{
        param:undefined,
        order: "asc"
    },

    /**
     * Sorts the current search/filter according to a parameter. By default results
     * are sorted in ascending order.Subsequent calls with the same parameter invert
     * the sort parameter.
     * @param sortParam
     * @param sortOrder
     */
    applySort:function (sortParam, sortOrder) {
        if(_.isUndefined(sortOrder) || (sortOrder !== this.scOrders.asc && sortOrder !== this.scOrders.desc)){
            this._sortParams.order = this.scOrders.asc;
        }else{
            this._sortParams.order = sortOrder;
        }

        this._sortParams.param = sortParam;

        return this.fetch();
    },

    /**
     * Resets the search params
     */
    resetSortParams:  function(){
        this._sortParams.param = undefined;
        this._sortParams.order = this.scOrders.asc;
    },

    addRoutes: function(router, ctx){

        if(_.isUndefined(ctx)){
            ctx = this;
        }

        router.route("s/:sortParam", "sort-default", function(sortParam){
            ctx.applySort(sortParam);
        });

        router.route("s/:sortParam/:sortOrder", "sort-order", function(sortParam, sortOrder){
            ctx.applySort(sortParam, sortOrder);
        });
    },

    initSortableLinks: function(targetElem, router) {
        targetElem.find("th a.ui-sort").on("click", function() {
            var oldFragment = $(this).attr("href");

            //Create a new fragment (inverting the order)
            var property = oldFragment.substring(0, oldFragment.lastIndexOf("/"));
            var currentSort = oldFragment.substring(oldFragment.lastIndexOf("/") + 1, oldFragment.length);
            var newSort = (currentSort === SortableCollection.scOrders.asc) ? SortableCollection.scOrders.desc : SortableCollection.scOrders.asc;

            //Switch the css classes on the link
            $(this).closest("th").removeClass(currentSort).addClass(newSort);

            //Replace the href property
            var fragment = property + "/" + newSort;
            $(this).attr("href", fragment);

            //Navigate using the old fragment
            router.navigate(oldFragment);
        });
    }

};

/**
 * Allows a collection to be paginated
 */
PaginatedCollection = {

    maxsteps: 10,

    page: 1,
    perPage: 10,
    total:0,

    parse: function(resp) {
        this.perPage = resp.perPage;
        this.page = resp.page;
        this.total = resp.total;

        log.trace("Per page: " + this.perPage);
        log.trace("Page: " + this.page);
        log.trace("Total: " + this.total);

        return resp.list;
    },

    url: function() {
        return this.baseUrl + '?' + $.param({page: this.page, perPage: this.perPage});
    },

    pageInfo: function() {

        var info = {
            total: this.total,
            page: this.page,
            perPage: this.perPage,
            pages: Math.ceil(this.total / this.perPage),
            prev: false,
            next: false
        };

        var max = Math.min(this.total, this.page * this.perPage);

        if (this.total == info.pages * this.perPage) {
            max = this.total;
        }

        info.range = [(this.page - 1) * this.perPage + 1, max];

        info.beginPage = 1;
        info.endPage = info.pages;

        if(info.pages > this.maxsteps) {
            info.beginPage = this.page - Math.round(this.maxsteps / 2) + (this.maxsteps % 2);
            info.endPage = this.page + Math.round(this.maxsteps / 2) - 1;
        }

        if (info.beginPage < 1) {
            info.beginPage = 1;
            info.endPage = this.maxsteps;
        }

        if (info.endPage > info.pages) {
            info.beginPage = info.pages - this.maxsteps + 1;
            if (info.beginPage < 1) {
                info.beginPage = 1;
            }
            info.endPage = info.pages;
        }

        info.prev = this.page > 1;
        info.next = this.page < info.pages;

        return info;
    },

    nextPage: function() {
        if (!this.pageInfo().next) {
            return false;
        }
        this.page = this.page + 1;
        return this.fetch();
    },

    previousPage: function() {
        if (!this.pageInfo().prev) {
            return false;
        }
        this.page = this.page - 1;
        return this.fetch();
    },

    goToPage: function(page) {
        this.page = page;
        return this.fetch();
    },

    resetPaging: function(){
        this.page = 1;
    },

    /**
     * Creates the routing and callbacks required for a collection to have pagination.
     * @param router
     * @param ctx
     */
    addRoutes: function(router, ctx){

        if(_.isUndefined(ctx)){
            ctx = this;
        }

        router.route("p/:page", "paging", function(page){
            ctx.goToPage(page);
        });
    }

};

/**
 * Allows a collection to be searched
 */
SearchableCollection = {

    query:  undefined,

    doSearch: function(query){
        this.query = query;
        return this.fetch();
    },

    resetSearch:function () {
        this.query = undefined;
    },

    addRoutes: function(router, ctx){

        if(_.isUndefined(ctx)){
            ctx = this;
        }

        router.route("q/:query", "search", function(query){
            ctx.doSearch(query);
        });
    }
};
