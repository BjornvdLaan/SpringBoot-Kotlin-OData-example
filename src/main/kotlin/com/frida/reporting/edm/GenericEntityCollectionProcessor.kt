package com.frida.reporting.edm

import com.frida.reporting.edm.providers.EntityProvider
import org.apache.olingo.commons.api.data.ContextURL
import org.apache.olingo.commons.api.data.EntityCollection
import org.apache.olingo.commons.api.format.ContentType
import org.apache.olingo.commons.api.http.HttpHeader
import org.apache.olingo.commons.api.http.HttpStatusCode
import org.apache.olingo.server.api.*
import org.apache.olingo.server.api.processor.EntityCollectionProcessor
import org.apache.olingo.server.api.serializer.EntityCollectionSerializerOptions
import org.apache.olingo.server.api.serializer.SerializerException
import org.apache.olingo.server.api.uri.UriInfo
import org.apache.olingo.server.api.uri.UriResourceEntitySet
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component


@Component
class GenericEntityCollectionProcessor(@Autowired val ctx: ApplicationContext) : EntityCollectionProcessor {

    private var odata: OData? = null
    private var serviceMetadata: ServiceMetadata? = null

    override fun init(odata: OData, serviceMetadata: ServiceMetadata) {
        this.odata = odata
        this.serviceMetadata = serviceMetadata
    }

    /**
     * This method is called when the user fires a request to a specific EntitySet
     * Example: http://localhost:8080/ExampleService1/ExampleServlet1.svc/Products
     */
    @Throws(ODataApplicationException::class, SerializerException::class)
    override fun readEntityCollection(request: ODataRequest,
                                      response: ODataResponse, uriInfo: UriInfo, responseFormat: ContentType) {

        // 1st we have to retrieve the requested EntitySet from the uriInfo object
        val resourcePaths = uriInfo.uriResourceParts
        val uriResourceEntitySet = resourcePaths[0] as UriResourceEntitySet // in our case, the first segment is the EntitySet
        val edmEntitySet = uriResourceEntitySet.entitySet

        // 2nd: fetch the data from backend for this requested EntitySetName
        var entityCollection: EntityCollection? = null
        val entityProviders = ctx
                .getBeansOfType(EntityProvider::class.java)
        for (entity in entityProviders.keys) {
            val entityProvider = entityProviders[entity]
            if (entityProvider!!.getEntityType().name == edmEntitySet.entityType.name) {
                entityCollection = entityProvider.getEntityCollection()
                break
            }
        }

        // 3rd: create a serializer based on the requested format (xml, json)
        val serializer = odata!!.createSerializer(responseFormat)

        // 4th: Now serialize the content: transform from the EntitySet object
        // to InputStream
        val edmEntityType = edmEntitySet.entityType
        val contextUrl = ContextURL.with().entitySet(edmEntitySet).build()

        val id = request.rawBaseUri.toString() + "/" + edmEntitySet.name
        val opts = EntityCollectionSerializerOptions.with().id(id).contextURL(contextUrl).build()
        val serializerResult = serializer.entityCollection(serviceMetadata, edmEntityType, entityCollection, opts)
        val serializedContent = serializerResult.content

        // Finally: configure the response object: set the body, headers and status code
        response.content = serializedContent;
        response.statusCode = HttpStatusCode.OK.statusCode;
        response.setHeader(HttpHeader.CONTENT_TYPE, responseFormat.toContentTypeString());
    }
}