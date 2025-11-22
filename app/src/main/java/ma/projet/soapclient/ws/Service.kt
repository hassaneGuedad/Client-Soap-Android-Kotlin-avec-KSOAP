package ma.projet.soapclient.ws

import ma.projet.soapclient.beans.Compte
import ma.projet.soapclient.beans.TypeCompte
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.StringReader
import java.text.SimpleDateFormat
import java.util.*

class Service {
    private val NAMESPACE = "http://ws.soapAcount/"
    private val URL = "http://10.0.2.2:8082/services/ws"
    private val client = OkHttpClient()

    // MODE DEMO: Mettre à true pour utiliser des données mockées sans serveur
    // Changez cette valeur à true si vous n'avez pas de serveur SOAP
    private val DEMO_MODE = true

    // Liste simulée pour le mode demo
    private val demoComptes = mutableListOf<Compte>()

    /**
     * Récupère la liste des comptes via le service SOAP.
     */
    fun getComptes(): List<Compte> {
        // MODE DEMO: Retourner des données mockées
        if (DEMO_MODE) {
            // Initialiser avec des comptes de demo si vide
            if (demoComptes.isEmpty()) {
                demoComptes.addAll(getDemoComptes())
            }
            return demoComptes.toList()
        }

        val soapRequest = """
            <?xml version="1.0" encoding="utf-8"?>
            <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
                <soap:Body>
                    <ns:getComptes xmlns:ns="$NAMESPACE" />
                </soap:Body>
            </soap:Envelope>
        """.trimIndent()

        val comptes = mutableListOf<Compte>()

        try {
            val response = sendSoapRequest(soapRequest)
            comptes.addAll(parseComptesResponse(response))
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return comptes
    }

    /**
     * Crée un nouveau compte via le service SOAP.
     * @param solde Solde initial du compte.
     * @param type Type du compte (COURANT ou EPARGNE).
     */
    fun createCompte(solde: Double, type: TypeCompte): Boolean {
        // MODE DEMO: Simuler l'ajout
        if (DEMO_MODE) {
            val newId = (demoComptes.maxOfOrNull { it.id ?: 0 } ?: 0) + 1
            val newCompte = Compte(
                id = newId,
                solde = solde,
                dateCreation = Date(),
                type = type
            )
            demoComptes.add(newCompte)
            return true
        }

        val soapRequest = """
            <?xml version="1.0" encoding="utf-8"?>
            <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
                <soap:Body>
                    <ns:createCompte xmlns:ns="$NAMESPACE">
                        <solde>$solde</solde>
                        <type>${type.name}</type>
                    </ns:createCompte>
                </soap:Body>
            </soap:Envelope>
        """.trimIndent()

        return try {
            sendSoapRequest(soapRequest)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Supprime un compte en fonction de son ID via le service SOAP.
     * @param id Identifiant du compte à supprimer.
     */
    fun deleteCompte(id: Long): Boolean {
        // MODE DEMO: Simuler la suppression
        if (DEMO_MODE) {
            return demoComptes.removeIf { it.id == id }
        }

        val soapRequest = """
            <?xml version="1.0" encoding="utf-8"?>
            <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
                <soap:Body>
                    <ns:deleteCompte xmlns:ns="$NAMESPACE">
                        <id>$id</id>
                    </ns:deleteCompte>
                </soap:Body>
            </soap:Envelope>
        """.trimIndent()

        return try {
            val response = sendSoapRequest(soapRequest)
            response.contains("true") || !response.contains("false")
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Envoie une requête SOAP et retourne la réponse.
     */
    private fun sendSoapRequest(soapXml: String): String {
        val mediaType = "text/xml; charset=utf-8".toMediaType()
        val body = soapXml.toRequestBody(mediaType)

        val request = Request.Builder()
            .url(URL)
            .post(body)
            .addHeader("Content-Type", "text/xml; charset=utf-8")
            .addHeader("SOAPAction", "")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                throw Exception("Unexpected code $response")
            }
            return response.body?.string() ?: ""
        }
    }

    /**
     * Parse la réponse XML pour extraire les comptes.
     */
    private fun parseComptesResponse(xml: String): List<Compte> {
        val comptes = mutableListOf<Compte>()

        try {
            val factory = XmlPullParserFactory.newInstance()
            val parser = factory.newPullParser()
            parser.setInput(StringReader(xml))

            var eventType = parser.eventType
            var currentCompte: MutableMap<String, String>? = null

            while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        val tagName = parser.name
                        if (tagName == "return") {
                            currentCompte = mutableMapOf()
                        } else if (currentCompte != null &&
                                   tagName in listOf("id", "solde", "dateCreation", "type")) {
                            parser.next()
                            if (parser.eventType == XmlPullParser.TEXT) {
                                currentCompte[tagName] = parser.text
                            }
                        }
                    }
                    XmlPullParser.END_TAG -> {
                        if (parser.name == "return" && currentCompte != null) {
                            try {
                                val compte = Compte(
                                    id = currentCompte["id"]?.toLongOrNull(),
                                    solde = currentCompte["solde"]?.toDoubleOrNull() ?: 0.0,
                                    dateCreation = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                        .parse(currentCompte["dateCreation"] ?: "") ?: Date(),
                                    type = TypeCompte.valueOf(currentCompte["type"] ?: "COURANT")
                                )
                                comptes.add(compte)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                            currentCompte = null
                        }
                    }
                }
                eventType = parser.next()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return comptes
    }

    /**
     * MODE DEMO: Retourne des comptes de démonstration
     */
    private fun getDemoComptes(): List<Compte> {
        val calendar = Calendar.getInstance()

        return listOf(
            Compte(
                id = 1,
                solde = 5000.0,
                dateCreation = Date(),
                type = TypeCompte.COURANT
            ),
            Compte(
                id = 2,
                solde = 10000.0,
                dateCreation = calendar.apply {
                    add(Calendar.DAY_OF_MONTH, -30)
                }.time,
                type = TypeCompte.EPARGNE
            ),
            Compte(
                id = 3,
                solde = 2500.50,
                dateCreation = calendar.apply {
                    set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 15)
                    add(Calendar.DAY_OF_MONTH, -15)
                }.time,
                type = TypeCompte.COURANT
            )
        )
    }
}