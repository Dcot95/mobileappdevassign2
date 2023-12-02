package org.wit.golfcourse.models

import timber.log.Timber.Forest.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class GolfcourseMemStore : GolfcourseStore {

    val golfcourses = ArrayList<GolfcourseModel>()

    override fun findAll(): List<GolfcourseModel> {
        return golfcourses
    }

    override fun create(golfcourse: GolfcourseModel) {
        golfcourse.id = getId()
        golfcourses.add(golfcourse)
        logAll()
    }

    override fun update(golfcourse: GolfcourseModel) {
        val foundGolfcourse: GolfcourseModel? = golfcourses.find { p -> p.id == golfcourse.id }
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
            logAll()
        }
    }

    private fun logAll() {
        golfcourses.forEach { i("$it") }
    }

    override fun delete(golfcourse: GolfcourseModel) {
        golfcourses.remove(golfcourse)
    }

    override fun findById(id:Long) : GolfcourseModel? {
        val foundGolfcourse: GolfcourseModel? = golfcourses.find { it.id == id }
        return foundGolfcourse
    }
}