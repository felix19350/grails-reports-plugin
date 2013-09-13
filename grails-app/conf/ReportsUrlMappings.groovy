class ReportsUrlMappings {

	static mappings = {

        name reportsList: "/reports"(controller: "report", parseRequest: true) {
            action = [GET: "list"]
        }

        //Sets the preview of the preview
        "/report/$id/setPreviewParams"(controller: "report", parseRequest: true) {
            action = [PUT: "setPreviewParams"]
        }

        //Previews a PDF. It returns the pdf
        "/report/$id/preview"(controller: "report") {
            action = [GET: "preview"]
        }

        //CRUD operations over a report
        "/report/$id?"(controller: "report", parseRequest: true) {
            action = [GET: "show", PUT: "update", POST: "save", DELETE: "delete"]
        }

        "/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}

		"/"(view:"/index")
		"500"(view:'/error')
	}
}
