package org.wit.golfcourse.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import org.wit.golfcourse.helpers.*
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*

const val JSON_FILE = "golfcourses.json"
val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .create()
val listType: Type = object : TypeToken<ArrayList<GolfcourseModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class GolfcourseJSONStore(private val context: Context) : GolfcourseStore {

    var golfcourses = mutableListOf<GolfcourseModel>()

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<GolfcourseModel> {
        logAll()
        return golfcourses
    }

    override fun create(golfcourse: GolfcourseModel) {
        golfcourse.id = generateRandomId()
        golfcourses.add(golfcourse)
        serialize()
    }

    override fun update(golfcourse: GolfcourseModel) {
        val golfcoursesList = findAll() as ArrayList<GolfcourseModel>
        var foundGolfcourse: GolfcourseModel? = golfcoursesList.find { p -> p.id == golfcourse.id }
        if (foundGolfcourse != null) {
            foundGolfcourse.title = golfcourse.title
            foundGolfcourse.description = golfcourse.description
            foundGolfcourse.dateplayed = golfcourse.dateplayed
            foundGolfcourse.price = golfcourse.price
            foundGolfcourse.rating = golfcourse.rating
            foundGolfcourse.image = golfcourse.image
            foundGolfcourse.lat = golfcourse.lat
            foundGolfcourse.lng = golfcourse.lng
            foundGolfcourse.zoom = golfcourse.zoom
        }
        serialize()
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(golfcourses, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        golfcourses = gsonBuilder.fromJson(jsonString, listType)
    }

    override fun delete(golfcourse: GolfcourseModel) {
        golfcourses.remove(golfcourse)
        serialize()
    }

    private fun logAll() {
        golfcourses.forEach { Timber.i("$it") }
    }

    override fun findById(id:Long) : GolfcourseModel? {
        val foundGolfcourse: GolfcourseModel? = golfcourses.find { it.id == id }
        return foundGolfcourse
    }
}

class UriParser : JsonDeserializer<Uri>,JsonSerializer<Uri> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Uri {
        return Uri.parse(json?.asString)
    }

    override fun serialize(
        src: Uri?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src.toString())
    }
}