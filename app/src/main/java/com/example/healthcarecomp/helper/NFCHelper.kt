package com.example.healthcarecomp.helper

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.nfc.FormatException
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.os.Parcelable
import android.util.Log
import android.widget.Toast
import com.example.healthcarecomp.ui.activity.auth.AuthViewModel
import com.example.healthcarecomp.ui.activity.main.MainViewModel
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import kotlin.experimental.and


/**
 * This class provides methods to access and manipulate NFC tags.
 *
 * @property authViewModel The authentication model used to check user access.
 * @property myTag The current NFC tag in use.
 */
class NFCHelper(
    private val authViewModel: AuthViewModel,
    private var myTag: Tag?,
    private val context: Context
) {
    /******************************************************************************
     * Read From NFC Tag
     ****************************************************************************/
    private val mainViewModel: MainViewModel? = null
    fun readFromIntent(intent: Intent) {
        val action = intent.action
        if (NfcAdapter.ACTION_TAG_DISCOVERED == action || NfcAdapter.ACTION_TECH_DISCOVERED == action || NfcAdapter.ACTION_NDEF_DISCOVERED == action) {
            authViewModel.SetOnBackPressedT()
            myTag = intent.getParcelableExtra<Parcelable>(NfcAdapter.EXTRA_TAG) as Tag?
            val rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            mainViewModel?.changeTag(tag = myTag)
            var msgs = mutableListOf<NdefMessage>()
            if (rawMsgs != null) {
                for (i in rawMsgs.indices) {
                    msgs.add(i, rawMsgs[i] as NdefMessage)
                }
                buildTagViews(msgs.toTypedArray())
            }
        }
    }


    private fun buildTagViews(msgs: Array<NdefMessage>) {
        var authMap = HashMap<String,String>()
        if (msgs == null || msgs.isEmpty()) return
        var text = ""
        for (i in 1 until msgs[0].records.size ) {
            val payload = msgs[0].records[i].payload
            val textEncoding: Charset =
                if ((payload[0] and 128.toByte()).toInt() == 0) Charsets.UTF_8 else Charsets.UTF_16 // Get the Text Encoding
            val languageCodeLength: Int =
                (payload[0] and 51).toInt() // Get the Language Code, e.g. "en"
            try {
                // Get the Text
                text =String(
                    payload,
                    languageCodeLength + 1,
                    payload.size - languageCodeLength - 1,
                    textEncoding
                )
                val textKey= text.substringBefore(":")
                val textVValue = text.substringAfter(":","")
                //  if (!textKey.take(3).equals("PSS")) {
                authMap[textKey] = textVValue
                //   }


            } catch (e: UnsupportedEncodingException) {
                Log.e("UnsupportedEncoding", e.toString())
            }
        }
        authViewModel.NFCValue.value = authMap
    }

    /******************************************************************************
     * Write to NFC Tag
     ****************************************************************************/
    @Throws(UnsupportedEncodingException::class)
    private fun createRecord(text: String): NdefRecord {
        val lang = "en"
        val textBytes = text.toByteArray()
        val langBytes = lang.toByteArray(charset("US-ASCII"))

        val langLength = langBytes.size
        val textLength = textBytes.size
        val payload = ByteArray(1 + langLength + textLength)

        // set status byte (see NDEF spec for actual bits)
        payload[0] = langLength.toByte()

        // copy langbytes and textbytes into payload
        System.arraycopy(langBytes, 0, payload, 1, langLength)
        System.arraycopy(textBytes, 0, payload, 1 + langLength, textLength)
        return NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, ByteArray(0), payload)
    }

    @Throws(UnsupportedEncodingException::class)
     fun writeMultipleRecords(appPath: String, phoneNumber: String, psd: String, pss: String, tag: Tag?) {
        val lang = "en"
        // Convert appPath, phoneNumber, psd, and pss to byte arrays
        val appPathBytes = appPath.toByteArray()
        val phoneNumberBytes = phoneNumber.toByteArray()
        val psdBytes = psd.toByteArray()
        val pssBytes = pss.toByteArray()

        // Convert lang to byte array using US-ASCII charset
        val langBytes = lang.toByteArray(charset("US-ASCII"))

        val records = Array<NdefRecord>(4) { NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, ByteArray(0), ByteArray(0)) }

        // Loop to create and add 4 records
        for ((index, dataBytes) in listOf(appPathBytes, phoneNumberBytes, psdBytes, pssBytes).withIndex()) {
            // Calculate lengths
            val langLength = langBytes.size
            val dataLength = dataBytes.size

            // Create payload
            val payload = ByteArray(1 + langLength + dataLength)
            payload[0] = langLength.toByte()
            System.arraycopy(langBytes, 0, payload, 1, langLength)
            System.arraycopy(dataBytes, 0, payload, 1 + langLength, dataLength)

            // Create NdefRecord and add to the array
            if (index == 0) {
                val intentUri = "com.example.healthcarecomp"
                val intentBytes = intentUri.toByteArray(charset("UTF-8"))
                val ndefRecord = NdefRecord.createApplicationRecord("com.example.healthcarecomp")
                //  records[index] = NdefRecord(NdefRecord.TNF_ABSOLUTE_URI, NdefRecord.RTD_URI, ByteArray(0), intentBytes)
                records[index] = ndefRecord
            }else records[index] = NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, ByteArray(0), payload)
        }
        val message = NdefMessage(records)
        // Get an instance of Ndef for the tag.
        val ndef = Ndef.get(tag)
        // Enable I/O
        ndef.connect()
        // Write the message
        ndef.writeNdefMessage(message)
        // Close the connection
        ndef.close()

    }

    fun handleError() {
        try {
            if (myTag == null) {
                Toast.makeText(context, ERROR_DETECTED, Toast.LENGTH_LONG).show()
            } else {
                writeMultipleRecords("com.easa", "01241414", "asdafasf", "1214", myTag)
                Toast.makeText(context, WRITE_SUCCESS, Toast.LENGTH_LONG).show()
            }
        } catch (e: IOException) {
            Toast.makeText(context, WRITE_ERROR, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        } catch (e: FormatException) {
            Toast.makeText(context, WRITE_ERROR, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }


    companion object {
        const val ERROR_DETECTED = "No NFC tag detected!"
        const val WRITE_SUCCESS = "Text written to the NFC tag successfully!"
        const val WRITE_ERROR = "Error during writing, is the NFC tag close enough to your device?"
    }




}