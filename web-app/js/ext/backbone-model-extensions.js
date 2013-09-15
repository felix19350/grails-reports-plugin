
ModelWithSubCollection = ((function(){

    "use strict";

    return {
        initializeSubCollection: function(name, collection, options) {
            var subCollection = this.get(name) || [];

            //log.debug("initializeSubCollection " + name);

            if(_.isArray(subCollection)) { // is not a collection
                log.debug("Create new collection " + name);
                var map = {};
                map[name] =  new collection(subCollection, options);
                this.set(map);
            }
        },

        parseSubCollection: function(response, name, collection, options) {
            var subCollection = response[name] || [];

            //log.debug("parseSubCollection " + name);

            if(!_.isUndefined(this.attributes)) { // model not created (collection call parser...)
                // the initialize will be called next
                if(this.has(name)) { // already have the collection
                    log.debug("Reset collection " + name + " with new values");
                    this.get(name).reset(subCollection);
                } else {
                    log.debug("Parse new collection " + name );
                    var map = {};
                    map[name] =  new collection(subCollection, options);
                    this.set(map);
                }

                delete response[name];

            }
        }

    };

})());