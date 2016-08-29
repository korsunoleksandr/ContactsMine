package db

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import entity.PhoneType

/**
 * Created by okorsun on 29.08.16.
 */

@DatabaseTable(tableName = Number.TABLE_NAME)
class Number {
    companion object{
        const val TABLE_NAME = "numbers"
    }

    @DatabaseField(generatedId = true)
    var id: Int = Int.MIN_VALUE

    @DatabaseField(canBeNull = false)
    var type: PhoneType = PhoneType.MOBILE

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    var contact: Contact? = null
}