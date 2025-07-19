package com.paraskcd.spotlightsearch.providers

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import com.paraskcd.spotlightsearch.types.ContextMenuAction
import com.paraskcd.spotlightsearch.types.SearchResult
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.net.toUri
import com.paraskcd.spotlightsearch.icons.PermDeviceInfo

@Singleton
class AppRepositoryProvider @Inject constructor(@ApplicationContext val context: Context) {
    private val packageManager: PackageManager = context.packageManager

    private val packageNameAliases: Map<String, List<String>> = mapOf(
        "app.revanced.manager.flutter" to listOf("manager", "revanced"),
        "com.google.android.accessibility.switchaccess" to listOf("accessibility", "accesibilidad", "interruptores", "con"),
        "com.google.android.dialer" to listOf("phone", "dialer", "call", "teléfono", "llamar"),
        "com.google.android.apps.messaging" to listOf("messages", "sms", "text", "mensajes"),
        "com.android.settings" to listOf("settings", "ajustes", "configuración"),
        "com.amazon.mShop.android.shopping" to listOf("amazon", "shopping", "compras"),
        "com.cirici.casaametller" to listOf("casa ametller", "casa", "ametller", "origen"),
        "com.dylan.win11.app.extractor" to listOf("apk", "extractor", "pro"),
        "com.google.android.apps.authenticator2" to listOf("authenticator", "autenticador", "2fa", "two factor", "google authenticator"),
        "com.azure.authenticator" to listOf("authenticator", "autenticador", "2fa", "two factor", "microsoft authenticator"),
        "com.backmarket" to listOf("backmarket", "back market", "backmarket.es", "backmarket france", "backmarket fr", "second hand", "segunda mano", "reacondicionado", "refurbished"),
        "consumerapp.bsmart.bcits.JVVNLcis" to listOf("jvvnl", "jvvnl cis", "jvvnl consumer app", "jvvnl app", "jvvnl rajasthan", "electricity", "electricidad", "electricity bill", "factura de electricidad"),
        "xyz.blueskyweb.app" to listOf("bluesky", "bluesky web", "bluesky social", "social media", "social", "redes sociales"),
        "com.bunq.android" to listOf("bunq", "banking", "banco", "banca", "finanzas"),
        "com.google.android.calculator" to listOf("calculator", "calculadora"),
        "com.canva.editor" to listOf("canva", "editor", "diseño", "design"),
        "com.caprabo.app" to listOf("caprabo", "supermercado", "supermarket", "compras", "groceries"),
        "cat.atm.cartera" to listOf("carta", "cartera", "atm", "atmcarta", "atmcarta.cat", "atmcarta app", "atm app", "wallet", "billete", "ticket", "transport", "public transport", "transporte publico"),
        "cat.atm.tmobilitat" to listOf("t-mobilitat", "transport", "public transport", "transporte publico"),"cat.gencat.mobi.rodalies" to listOf("rodalies", "transport", "public transport", "transporte publico"),
        "club.incendiarypigs.mur" to listOf("mur"),
        "cn.gavinliu.snapmod" to listOf("snapmod"),
        "com.Oney.OneyApp" to listOf("españa", "oney", "banking", "banco", "banca", "finanzas"),
        "com.Slack" to listOf("slack", "chat", "mensajería", "messaging", "work"),
        "com.automattic.simplenote" to listOf("simplenote"),
        "com.b_lam.resplash" to listOf("resplash"),
        "com.einnovation.temu" to listOf("temu"),
        "com.geomobile.tmbmobile" to listOf("tmbapp"),
        "com.google.android.apps.pixel.health" to listOf("termómetro"),
        "com.google.android.apps.playconsole" to listOf("play", "console"),
        "com.google.android.apps.wearables.maestro.companion" to listOf("pixel", "buds"),
        "com.google.audio.hearing.visualization.accessibility.scribe" to listOf("sonidos", "notificaciones", "instantánea", "de", "transcripción", "y"),
        "com.instagram.barcelona" to listOf("threads", "ig", "instagram", "social", "social media", "redes sociales"),
        "com.paraskcd.unitedwalls" to listOf("walls", "united", "wallpapers"),
        "com.paypal.android.p2pmobile" to listOf("paypal", "banking", "banco", "banca", "finanzas"),
        "es.bancosantander.apps" to listOf("santander", "banking", "banco", "banca", "finanzas"),
        "com.picsart.studio" to listOf("picsart", "editing", "photo", "fotografía", "editor de fotos"),
        "com.rebtel.android" to listOf("rebtel", "llamadas", "calls", "international calls", "llamadas internacionales", "mobile recharge", "recarga móvil"),
        "com.reddit.frontpage" to listOf("reddit", "social", "redes sociales", "social media", "community", "communities", "subreddit"),"com.remitly.androidapp" to listOf("remitly", "money transfer", "transferencia de dinero", "envío de dinero", "finanzas", "banco", "banking"),
        "com.sequra.app" to listOf("sequra", "banking", "banco", "banca", "finanzas"),
        "com.shazam.android" to listOf("shazam", "music", "detect music"),
        "com.ss.squarehome2" to listOf("home", "square", "launcher"),
        "com.squarespace.android.squarespaceapp" to listOf("squarespace", "compras", "domain", "dns"),
        "com.suno.android" to listOf("suno", "ai", "inteligencia artificial", "gpt", "artificial", "artificial intelligence", "robot"),
        "com.tailscale.ipn" to listOf("tailscale", "seguridad", "vpn", "connect", "self hosted"),"com.termux" to listOf("termux", "terminal", "linux", "command line", "cli", "shell", "hacker", "hacking"),
        "com.tinder" to listOf("tinder", "dating", "social", "redes sociales", "social media"),
        "com.tozelabs.tvshowtime" to listOf("time", "tv", "social", "redes sociales", "social media", "tvshowtime"),
        "com.toggl.giskard" to listOf("toggltrack", "work", "time", "reloj"),
        "com.westernunion.moneytransferr3app.es" to listOf("westernunion", "money transfer", "transferencia de dinero", "envío de dinero", "finanzas", "banco", "banking"),
        "com.whicons.iconpack" to listOf("whicons", "icon pack"),
        "com.wireguard.android" to listOf("wireguard", "seguridad", "vpn", "connect"),
        "de.number26.android" to listOf("n26", "home banking", "banco", "finanzas", "banking"),
        "it.very.mobile" to listOf("very", "italy", "telecomunicaciones", "telecom", "telefonía", "mobile"),
        "net.openvpn.openvpn" to listOf("security", "openvpn", "seguridad", "vpn", "connect"),
        "com.cetelem.HomeBanking" to listOf("cetelem", "home banking", "banco", "finanzas", "banking"),
        "com.android.chrome" to listOf("chrome", "browser", "navegador", "web"),
        "es.cineyelmo" to listOf("cine", "yelmocine", "yelmocine.es", "yelmocine app", "yelmocine cinema", "yelmocine movie", "movie tickets", "entradas de cine"),
        "es.aeat.pin24h" to listOf("clave"),
        "es.gob.afirma" to listOf("firma", "firma electrónica", "firma digital", "afirma"),
        "com.discord" to listOf("discord", "chat", "comunidad", "server", "servidor"),"com.google.android.apps.docs.editors.docs" to listOf("docs", "documentos", "google docs", "editor de documentos"),
        "com.google.android.apps.docs.editors.sheets" to listOf("sheets", "hojas de cálculo", "google sheets", "editor de hojas de cálculo"),
        "com.google.android.apps.docs.editors.slides" to listOf("slides", "presentaciones", "google slides", "editor de presentaciones"),
        "com.google.android.apps.docs" to listOf("drive", "google drive", "almacenamiento", "storage"),
        "com.google.android.apps.weather" to listOf("weather", "clima", "tiempo", "meteorología"),
        "com.google.android.apps.safetyhub" to listOf("safety", "seguridad", "safety hub", "seguridad hub", "sos"),
        "com.mttnow.android.etihad" to listOf("etihad", "etihad airways", "airlines", "aerolínea", "vuelo", "flight"),
        "com.google.android.apps.nbu.files" to listOf("files", "archivos", "file manager", "gestor de archivos"),
        "org.mozilla.firefox" to listOf("firefox", "browser", "navegador", "web"),
        "com.google.android.apps.photos" to listOf("photos", "fotos", "galería", "google photos"),
        "com.google.android.apps.photosgo" to listOf("photos", "fotos", "galería", "google photos"),
        "com.google.android.apps.bard" to listOf("bard", "google bard", "ai", "inteligencia artificial", "gpt", "artificial", "artificial intelligence", "robot"),
        "com.openai.chatgpt" to listOf("chatgpt", "openai", "inteligencia artificial", "ai", "gpt", "artificial", "artificial intelligence", "robot", "bots"),
        "ai.x.grok" to listOf("grok", "x", "ai", "x.com", "inteligencia artificial", "gpt", "artificial", "artificial intelligence", "robot", "bots"),
        "com.github.android" to listOf("github", "git", "repositorio", "repository", "código", "code", "work"),
        "com.glovo" to listOf("glovo", "delivery", "entrega", "reparto", "food delivery"),
        "com.google.android.gm" to listOf("gmail", "email", "correo", "mail", "work"),
        "com.google.android.googlequicksearchbox" to listOf("search", "google search", "búsqueda", "google"),
        "com.google.android.apps.magazines" to listOf("news", "noticias", "google news", "revistas", "magazines"),
        "com.android.vending" to listOf("play store", "google play", "store", "tienda", "apps"),
        "com.google.android.videos" to listOf("google videos", "vídeos", "video"),
        "com.google.android.apps.walletnfcrel" to listOf("wallet", "google wallet", "pago", "payments", "pagos"),
        "com.google.android.apps.recorder" to listOf("recorder", "grabadora", "grabación", "audio recorder"),
        "com.gumroad.app" to listOf("gumroad", "compras", "shopping", "marketplace"),
        "org.illegaller.ratabb.hishoot2i" to listOf("hishoot2i", "hishoot", "ratabb", "camera", "fotografía", "photo"),
        "com.holded.app" to listOf("holded", "finanzas", "banco", "banking", "contabilidad", "accounting", "salary", "nómina", "invoices", "facturas", "salario"),"com.google.android.apps.chromecast.app" to listOf("chromecast", "cast", "streaming", "google cast", "google home"),
        "com.idealista.android" to listOf("idealista", "inmobiliaria", "real estate", "propiedades", "properties"),
        "com.instagram.android" to listOf("instagram", "ig", "social media", "redes sociales", "social"),
        "com.atlassian.android.jira.core" to listOf("jira", "atlassian", "project management", "gestión de proyectos", "task management", "gestión de tareas", "agile", "scrum", "work"),
        "com.kunzisoft.keepass.free" to listOf("keepass", "password manager", "gestor de contraseñas", "passwords"),
        "es.kfc.spain" to listOf("kfc", "fast food", "comida rápida", "pollo", "chicken"),
        "com.myklarnamobile" to listOf("klarna", "finanzas", "banco", "banking", "compras", "shopping"),
        "cat.gencat.mobi.lamevasalut" to listOf("meu salut", "meu salut app", "salut", "salud", "health", "sanidad", "cat salut"),
        "com.readygo.barrel.gp" to listOf("barrel", "barrel app", "barrel game", "juego", "game"),
        "com.linkedin.android" to listOf("linkedin", "social media", "redes sociales", "networking", "empleo", "jobs"),
        "com.google.android.apps.adm" to listOf("find my device", "find my phone", "google find my device", "localización", "ubicación", "device manager"),
        "com.google.android.apps.maps" to listOf("google maps", "maps", "mapas", "navegación", "navigation"),
        "org.joinmastodon.android" to listOf("mastodon", "social media", "redes sociales", "fediverse", "social"),
        "me.zhanghai.android.files" to listOf("files", "archivos", "file manager", "gestor de archivos"),
        "com.google.android.apps.tachyon" to listOf("duo", "google duo", "videollamadas", "videocalls", "video calls"),
        "es.mercadona.tienda" to listOf("mercadona", "supermercado", "supermarket", "compras", "groceries"),
        "com.movistar.android.mimovistar.es" to listOf("mi movistar", "movistar", "telecomunicaciones", "telecom", "telefonía", "mobile"),
        "com.orange.miorange" to listOf("mi orange", "orange", "telecomunicaciones", "telecom", "telefonía", "mobile"),
        "es.vodafone.mobile.mivodafone" to listOf("mi vodafone", "vodafone", "telecomunicaciones", "telecom", "telefonía", "mobile"),
        "com.realtimeboard" to listOf("miro", "miro app", "collaboration", "colaboración", "whiteboard", "pizarra"),
        "net.mullvad.mullvadvpn" to listOf("mullvad", "vpn", "virtual private network", "red privada virtual"),
        "com.google.android.calendar" to listOf("calendar", "calendario", "agenda", "google calendar", "eventos", "events"),
        "com.google.android.GoogleCamera" to listOf("camera", "photo", "foto", "cámara", "fotografía", "google camera"),
        "com.google.android.deskclock" to listOf("clock", "reloj", "alarm", "alarma", "timer", "cronómetro"),
        "com.google.android.youtube" to listOf("youtube", "yt", "vídeos", "video", "streaming", "google youtube"),
        "tv.twitch.android.app" to listOf("streaming", "vídeos", "video", "twitch"),
        "com.google.android.apps.translate" to listOf("translate", "traductor", "traducción", "google translate", "google traductor"),
        "com.google.android.contacts" to listOf("contacts", "contactos", "agenda", "teléfono", "phone"),
        "com.google.android.keep" to listOf("notes", "notas", "keep", "google keep"),
        "com.google.android.apps.youtube.music" to listOf("music", "player", "música", "yt music", "youtube music", "yt"),
        "com.whatsapp" to listOf("whatsapp", "chat", "mensaje", "mensajería", "whatsapp messenger", "wa"),
        "com.twitter.android" to listOf("twitter", "x", "tweets", "red social", "social media", "social"),
        "com.facebook.katana" to listOf("facebook", "fb", "social", "redes sociales", "social media"),
        "org.telegram.messenger" to listOf("telegram", "chat", "mensajería", "messaging", "tg"),
        "tw.nekomimi.nekogram" to listOf("telegram", "chat", "mensajería", "messaging", "tg", "nekogram", "neko")
    )

    private val cachedApps: List<SearchResult> by lazy {
        val apps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        apps.mapNotNull { appInfo ->
            val launchIntent = packageManager.getLaunchIntentForPackage(appInfo.packageName)
            if (launchIntent != null) {
                val label = appInfo.loadLabel(packageManager).toString()
                val icon = appInfo.loadIcon(packageManager)

                SearchResult(
                    title = label,
                    subtitle = appInfo.packageName,
                    icon = icon,
                    onClick = {
                        launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(launchIntent)
                    },
                    contextMenuActions = buildList {
                        add(
                            ContextMenuAction(
                                title = "App Info",
                                icon = PermDeviceInfo,
                                onClick = {
                                    val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                        data = "package:${appInfo.packageName}".toUri()
                                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    }
                                    context.startActivity(intent)
                                }
                            )
                        )
                    }
                )
            } else null
        }
    }

    fun searchInstalledApp(query: String): List<SearchResult> {
        if (query.isBlank()) return emptyList()

        return cachedApps.filter { app ->
            app.title.contains(query, ignoreCase = true) ||
            app.subtitle?.let { packageName ->
                packageNameAliases[packageName]?.any { alias ->
                    alias.contains(query, ignoreCase = true)
                }
            } == true
        }
    }
}