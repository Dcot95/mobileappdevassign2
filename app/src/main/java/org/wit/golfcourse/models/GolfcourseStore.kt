package org.wit.golfcourse.models

interface GolfcourseStore {
    fun findAll(): List<GolfcourseModel>
    fun create(golfcourse: GolfcourseModel)
    fun update(golfcourse: GolfcourseModel)
    fun delete(golfcourse: GolfcourseModel)
    fun findById(id:Long) : GolfcourseModel?
}