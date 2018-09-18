package safira.meetme.model.services

// dummy friend location provider by Caspar for MAD s2, 2017
// Usage: add this class to project in appropriate package
// add dummy_data.txt to res/raw folder
// see: TestLocationService.test() method for example

// NOTE: you may need to expliity add the import for the generated some.package.R class
// which is based on your package declaration in the manifest
import android.content.Context
import android.content.res.Resources
import android.util.Log

import java.text.DateFormat
import java.text.ParseException
import java.util.ArrayList
import java.util.Calendar
import java.util.Date
import java.util.LinkedList
import java.util.Scanner

import safira.meetme.R

class DummyLocationService// Singleton
private constructor() {
    private val locationList = LinkedList<FriendLocation>()

    // This is only a data access object (DAO)
    // You must extract data and place in your model
    class FriendLocation {
        var time: Date? = null
        var id: String? = null
        var name: String? = null
        var latitude: Double = 0.toDouble()
        var longitude: Double = 0.toDouble()

        override fun toString(): String {
            return String.format("Time=%s, id=%s, name=%s, lat=%.5f, long=%.5f", DateFormat.getTimeInstance(
                    DateFormat.MEDIUM).format(time), id, name, latitude, longitude)
        }
    }

    // check if the source time is with the range of target time +/- minutes and seconds
    private fun timeInRange(source: Date?, target: Date, periodMinutes: Int, periodSeconds: Int): Boolean {
        val sourceCal = Calendar.getInstance()
        sourceCal.time = source

        // set up start and end range match
        // +/- period minutes/seconds to check
        val targetCalStart = Calendar.getInstance()
        targetCalStart.time = target
        targetCalStart.set(Calendar.MINUTE, targetCalStart.get(Calendar.MINUTE) - periodMinutes)
        targetCalStart.set(Calendar.SECOND, targetCalStart.get(Calendar.SECOND) - periodSeconds)
        val targetCalEnd = Calendar.getInstance()
        targetCalEnd.time = target
        targetCalEnd.set(Calendar.MINUTE, targetCalEnd.get(Calendar.MINUTE) + periodMinutes)
        targetCalEnd.set(Calendar.SECOND, targetCalEnd.get(Calendar.SECOND) + periodMinutes)

        // return if source time in the target range
        return sourceCal.after(targetCalStart) && sourceCal.before(targetCalEnd)
    }

    // called internally before usage
    private fun parseFile(context: Context?) {
        locationList.clear()
        // resource reference to dummy_data.txt in res/raw/ folder of your project
        try {
            Scanner(context!!.resources.openRawResource(R.raw.dummy_data)).use { scanner ->
                // match comma and 0 or more whitepace (to catch newlines)
                scanner.useDelimiter(",\\s*")
                while (scanner.hasNext()) {
                    val friend = FriendLocation()
                    friend.time = DateFormat.getTimeInstance(DateFormat.MEDIUM).parse(scanner.next())
                    friend.id = scanner.next()
                    friend.name = scanner.next()
                    friend.latitude = java.lang.Double.parseDouble(scanner.next())
                    friend.longitude = java.lang.Double.parseDouble(scanner.next())
                    locationList.addLast(friend)
                }
            }
        } catch (e: Resources.NotFoundException) {
            Log.i(LOG_TAG, "File Not Found Exception Caught")
        } catch (e: ParseException) {
            Log.i(LOG_TAG, "ParseException Caught (Incorrect File Format)")
        }

    }

    // singleton support
    private object LazyHolder {
        internal val INSTANCE = DummyLocationService()
    }

    // log contents of file (for testing/logging only)
    fun logAll() {
        log(locationList)
    }

    // log contents of provided list (for testing/logging and example purposes only)
    fun log(locationList: List<FriendLocation>) {
        // we reparse file contents for latest data on every call
        parseFile(context)
        for (friend in locationList)
            Log.i(LOG_TAG, friend.toString())
    }

    // the main method you can call periodcally to get data that matches a given time period
    // time +/- period minutes/seconds to check
    fun getFriendLocationsForTime(time: Date, periodMinutes: Int, periodSeconds: Int): List<FriendLocation> {
        // we reparse file contents for latest data on every call
        parseFile(context)
        val returnList = ArrayList<FriendLocation>()
        for (friend in locationList)
            if (timeInRange(friend.time, time, periodMinutes, periodSeconds))
                returnList.add(friend)
        return returnList
    }

    companion object {
        // PRIVATE PORTION
        private val LOG_TAG = DummyLocationService::class.java.name
        private var context: Context? = null

        // PUBLIC METHODS

        // singleton
        // thread safe lazy initialisation: see https://en.wikipedia.org/wiki/Initialization-on-demand_holder_idiom
        fun getSingletonInstance(context: Context): DummyLocationService {
            DummyLocationService.context = context
            return LazyHolder.INSTANCE
        }
    }
}
