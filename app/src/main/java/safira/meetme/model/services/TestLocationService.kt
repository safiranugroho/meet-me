package safira.meetme.model.services

import android.content.Context
import android.util.Log

import java.text.DateFormat
import java.text.ParseException

// Support code by Caspar for MAD assignment s2 2017
// Simple example to test dummy location service (for demonstration only)
// Usage: add class in appropriate package (also see TestLocationService.java)
//       TestLocationService.test(this); // pass a valid Context (NOTE: Activity extends Context)
object TestLocationService {
    private val LOG_TAG = DummyLocationService::class.java.name

    // call this method to run simple hard coded test
    fun test(context: Context) {
        val dummyLocationService = DummyLocationService.getSingletonInstance(context)

        Log.i(LOG_TAG, "File Contents:")
        dummyLocationService.logAll()
        var matched: List<DummyLocationService.FriendLocation>? = null
        try {
            // 2 mins either side of 9:46:30 AM
            matched = dummyLocationService.getFriendLocationsForTime(DateFormat.getTimeInstance(
                    DateFormat.MEDIUM).parse("9:46:00 AM"), 2, 0)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        Log.i(LOG_TAG, "Matched Query:")
        dummyLocationService.log(matched!!)
    }
}
