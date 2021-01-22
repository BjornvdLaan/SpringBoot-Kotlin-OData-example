package com.frida.reporting.controller

import org.apache.olingo.commons.api.edm.provider.CsdlAbstractEdmProvider
import org.apache.olingo.server.api.OData
import org.apache.olingo.server.api.processor.EntityCollectionProcessor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletRequestWrapper
import javax.servlet.http.HttpServletResponse


@RestController
@RequestMapping(ODataController.URI)
class ODataController {
    @Autowired
    private val edmProvider: CsdlAbstractEdmProvider? = null

    @Autowired
    private val entityCollectionProcessor: EntityCollectionProcessor? = null

    @RequestMapping(value = ["*"])
    fun process(request: HttpServletRequest?, response: HttpServletResponse?) {
        val odata = OData.newInstance()
        val edm = odata.createServiceMetadata(edmProvider,
                ArrayList())
        val handler = odata.createHandler(edm)
        handler.register(entityCollectionProcessor)
        handler.process(object : HttpServletRequestWrapper(request) {
            // Spring MVC matches the whole path as the servlet path
            // Olingo wants just the prefix, ie upto /odata, so that it
            // can parse the rest of it as an OData path. So we need to override
            // getServletPath()
            override fun getServletPath(): String {
                return URI
            }
        }, response)
    }

    companion object {
        const val URI = "/odata.svc"
    }
}