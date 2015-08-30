import sample.app.SearchModule
import sample.app.ISearchService

import static ratpack.groovy.Groovy.ratpack

ratpack {

	bindings {
		module SearchModule
	}

	handlers {
		get() {
			render "pybot -d result keyword_search.txt".execute().text
		}

		prefix("keyword") {
			get(":value?") { ISearchService searchService ->
				def keyword = pathTokens.value ?: "Ruckus Wireless"
				def limit = (request.queryParams.limit ?: 10).toInteger()

				render searchService.execute(keyword, limit)
			}
		}
	}

}
