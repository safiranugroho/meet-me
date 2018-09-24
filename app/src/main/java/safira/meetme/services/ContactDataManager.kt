package safira.meetme.services

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.provider.ContactsContract.CommonDataKinds.Email
import android.provider.ContactsContract.Contacts
import android.util.Log

class ContactDataManager
(private val context: Context, private val intent: Intent) {
    companion object{
        private const val LOG_TAG: String = "ContactDataManager"
    }

    val contactName: String?
        @Throws(ContactQueryException::class)
        get() {
            var cursor: Cursor? = null
            var name: String? = null

            try {
                cursor = context.contentResolver.query(intent.data!!, null, null, null, null)

                if (cursor!!.moveToFirst())
                    name = cursor.getString(cursor
                            .getColumnIndexOrThrow(Contacts.DISPLAY_NAME))

            } catch (e: Exception) {
                Log.e(LOG_TAG, e.message)
                throw ContactQueryException(e.message.toString())

            } finally {
                cursor?.close()
            }

            return name
        }

    val contactEmail: String?
        @Throws(ContactQueryException::class)
        get() {
            var cursor: Cursor? = null
            var email: String? = null
            try {

                cursor = context.contentResolver.query(Email.CONTENT_URI, null, Email.CONTACT_ID + "=?",
                        arrayOf(intent.data!!.lastPathSegment), null)

                if (cursor!!.moveToFirst())
                    email = cursor.getString(cursor.getColumnIndex(Email.DATA))

            } catch (e: Exception) {
                Log.e(LOG_TAG, e.message)
                throw ContactQueryException(e.message.toString())

            } finally {
                cursor?.close()
            }

            return email
        }

    inner class ContactQueryException(message: String) : Exception(message)
}