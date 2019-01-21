package io.github.luteoos.gent.data

import io.github.luteoos.gent.network.api.dataobjects.PersonDTO

object PersonListFromTree {
    const val RELATION_MARRIAGE = "Marriage"
    const val RELATION_CHILD = "Child"
    const val RELATION_PARENT = "Parent"
    const val RELATION_SIBLING = "Sibling"

    const val PERSON_MALE = "Male"
    const val PERSON_FEMALE = "Female"

    var list: MutableList<PersonDTO>? = null


    fun isFilled(): Boolean{
        if (list != null)
            return true
        return false
    }

    fun Clear(){
        list = null
    }

    fun getPerson(searchedID: String): PersonDTO?{
        return list?.findLast { it.id == searchedID }
    }

    fun getPersonRelativesOfTypeUUIDList(searchedID: String, relativeType: String): MutableList<String>{
        val kid = getPerson(searchedID)
        val relatives : MutableList<String> = MutableList(0){ _ ->""}
        kid?.relations?.filter { it.type == relativeType }?.forEach { relatives.add(it.secondPersonId) }
        return relatives
    }
}