package safira.meetme.model.services

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.provider.ContactsContract.CommonDataKinds.Email
import android.provider.ContactsContract.Contacts
import android.util.Log

/**
 *
 *
 * Class used to retrieve information about a Contact selected through the Android
 * Contacts Picker Activity. requires permission android.permission.READ_CONTACTS
 *
 *
 *
 * Simplified manifest permissions for targetSdkVersion<=22
 * <uses-permission android:name="android.permission.READ_CONTACTS"></uses-permission>
 *
 * Below is a simple example usage of this class from an Activity class
 *
 *
 * launch the contact picker and to promote the user to select an attendee
 *
 *
 * `
 * protected static final int PICK_CONTACTS = 100;
` *
 *
 * Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
 * startActivityForResult(contactPickerIntent, PICK_CONTACTS);
 *
 *
 *
 * grab the result in onActivityResult()
 * `
 *
 * @author ermyasabebe
 * @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
 * if (requestCode == PICK_CONTACTS) {
 * if (resultCode == RESULT_OK) {
 * ContactDataManager contactsManager = new ContactDataManager(this, data);
 * String name = "";
 * String email = "";
 * try {
 * name = contactsManager.getContactName();
 * email = contactsManager.getContactEmail();
 * } catch (ContactQueryException e) {
 * Log.e(LOG_TAG, e.getMessage());
 * }
` *
 *
 * }
 *
 *
 * }
 * }
 *
 */
class ContactDataManager
/**
 * @param aContext The context through which the Android Contacts Picker Activity
 * was launched
 * @param anIntent The intent returned from the Android Contacts Picker Activity
 */
(private val context: Context, private val intent: Intent) {

    /**
     * Retrieves the display Name of a contact
     *
     * @return Name of the contact referred to by the URI specified through the
     * intent, [ContactDataManager.intent]
     * @throws ContactQueryException if querying the Contact Details Fails
     */
    val contactName: String?
        @Throws(ContactQueryException::class)
        get() {
            var cursor: Cursor? =
                    null
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

    /**
     * Retrieves the email of a contact
     *
     * @return Email of the contact referred to by the URI specified through the
     * intent, [ContactDataManager.intent]
     * @throws ContactQueryException if querying the Contact Details Fails
     */
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

    companion object {
        private val LOG_TAG = ContactDataManager::class.java.name
    }

}