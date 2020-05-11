package com.example.bundler

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.hendraanggrian.bundler.extrasOf

class MainFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.fragment_main)
        findPreference<Preference>("option1")!!.setOnPreferenceClickListener {
            false
        }
        findPreference<Preference>("option2")!!.setOnPreferenceClickListener {
            // startActivity(Intent(context, Option2Activity::class.java))
            false
        }
    }

    class InputDialogFragment : DialogFragment() {

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val dialogView = layoutInflater.inflate(R.layout.fragment_input, null)
            val inputTitleView = dialogView.findViewById<EditText>(R.id.inputTitleView)
            val inputSubtitleView = dialogView.findViewById<EditText>(R.id.inputSubtitleView)
            return MaterialAlertDialogBuilder(context)
                .setView(dialogView)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    val intent = Intent(context, Example1Activity::class.java)
                    intent.putExtras(
                        extrasOf<Example1Activity>(
                            if (inputTitleView.text.isNotBlank()) inputTitleView.text else "Empty title",
                            if (inputSubtitleView.text.isNotBlank()) inputSubtitleView.text else "null"
                        )
                    )
                    startActivity(intent)
                }
                .create()
        }
    }
}