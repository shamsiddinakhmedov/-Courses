package com.example.pdponline.databse.dao

import androidx.room.*
import com.example.pdponline.databse.Module
import com.example.pdponline.databse.ModuleWithLesson
import io.reactivex.Flowable

@Dao
interface ModuleDao {
    @Transaction
    @Query("select * from module where coursesOwnerId =:id")
    fun getAllModulesById(id: Int): List<ModuleWithLesson>

    @Insert
    fun insertModule(module: Module)

    @Delete
    fun deleteModule(module: Module)

    @Update
    fun updateModule(module: Module)

    @Query("select id from module where coursesOwnerId =:cID")
    fun getModuleId(cID: Int): Int

    @Query("select * from module where id =:id")
    fun getModuleById(id: Int): Module

    @Query("select * from module where coursesOwnerId = :id order by placeNumber")
    fun getAllModules(id: Int): Flowable<List<Module>>

    @Query("select * from module where coursesOwnerId = :id order by placeNumber")
    fun getAllModulesByOrder(id: Int): Flowable<List<Module>>

    @Query("select * from module where coursesOwnerId = :id order by placeNumber")
    fun getModules(id: Int): List<Module>
}