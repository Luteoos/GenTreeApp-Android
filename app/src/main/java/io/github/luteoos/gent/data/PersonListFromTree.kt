package io.github.luteoos.gent.data

import android.content.Context
import io.github.luteoos.gent.R
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

    fun getPersonBirthDate(person: PersonDTO, context: Context): String{
        val event = person.details.events.findLast { it.type == "Birth" }
        return event?.getFormattedDate() ?: context.getString(R.string.no_information)
    }

    fun getPersonDeathDate(person: PersonDTO, context: Context): String{
        val event = person.details.events.findLast { it.type == "Death" }
        return event?.getFormattedDate() ?: context.getString(R.string.no_information)
    }

    fun getPersonRelativesOfTypeUUIDList(searchedID: String, relativeType: String): MutableList<String>{
        val kid = getPerson(searchedID)
        val relatives : MutableList<String> = MutableList(0){ _ ->""}
        kid?.relations?.filter { it.type == relativeType }?.forEach { relatives.add(
            if(it.secondPersonId != searchedID)
                it.secondPersonId
            else
                it.firstPersonId)}
        return relatives
    }
}