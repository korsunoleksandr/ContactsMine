package db

import com.j256.ormlite.dao.ForeignCollection
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.field.ForeignCollectionField
import com.j256.ormlite.table.DatabaseTable

/**
 * Created by okorsun on 29.08.16.
 */

@DatabaseTable(tableName = Contact.TABLE_NAME)
class Contact {
    companion object{
        const val TABLE_NAME = "contacts"
    }

    @DatabaseField(generatedId = true)
    var id: Int = Int.MIN_VALUE

    @DatabaseField
    var name: String? = null

    @DatabaseField
    var imageUrl: String? = null

    @ForeignCollectionField(eager = false)
    var numbers: ForeignCollection<Number>? = null

}