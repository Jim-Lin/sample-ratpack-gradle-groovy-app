package sample.app

import groovyx.net.http.HTTPBuilder
import groovyx.net.http.ContentType
import groovyx.net.http.Method

class ApiConsumer {

    static def getJson(String baseUrl, String path, query) {
        try {
            def http = new HTTPBuilder()

            http.request(baseUrl, Method.GET, ContentType.JSON) { req ->
                uri.path = path
                uri.query = query
                headers.'User-Agent' = "Mozilla/5.0 Firefox/3.0.4"
                headers.Accept = 'application/json'

                response.success = { resp, json ->
                    assert resp.statusLine.statusCode == 200
                    println "Got response: ${resp.statusLine}"
                    println "Content-Type: ${resp.headers.'Content-Type'}"

                    return json
                }

                response.'404' = {
                    return null
                }
            }
        } catch (groovyx.net.http.HttpResponseException ex) {
            ex.printStackTrace()
            return null
        } catch (java.net.ConnectException ex) {
            ex.printStackTrace()
            return null
        }
    }
}