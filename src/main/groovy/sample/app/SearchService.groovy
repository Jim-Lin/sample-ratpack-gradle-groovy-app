package sample.app

import groovy.json.JsonOutput

class SearchService implements ISearchService {

    String execute(String keyword, int limit) {
        Properties properties = new Properties()
        File propertiesFile = new File('src/ratpack/ratpack.properties')
        propertiesFile.withInputStream {
            properties.load(it)
        }

        def url = "https://www.googleapis.com"
        def path = "/customsearch/v1"
        def query = [ key: properties.google_search_key, cx: properties.google_search_cx, q: keyword, num: limit ]

        // Submit a request via GET
        def result = ApiConsumer.getJson(url, path, query)

        def sites = []
        if (result != null) {
            result.items.each {
                sites.add(new Site(title: it.title, link: it.link))
            }
        }

        return JsonOutput.toJson([data: sites])
    }

}
