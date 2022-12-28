package com.example

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.hendraanggrian.auto.bundler.extrasOf

class MainFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.fragment_main)
        findPreference<Preference>("example1")!!.setOnPreferenceClickListener {
            InputDialogFragment().show(childFragmentManager, null)
            false
        }
        findPreference<Preference>("example2")!!.setOnPreferenceClickListener {
            startActivity(Intent(context, Example2Activity::class.java))
            false
        }
    }

    class InputDialogFragment : DialogFragment() {

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val dialogView = layoutInflater.inflate(R.layout.fragment_input, null)
            val inputTitleView = dialogView.findViewById<EditText>(R.id.inputTitleView)
            val inputSubtitleView = dialogView.findViewById<EditText>(R.id.inputSubtitleView)
            return MaterialAlertDialogBuilder(requireContext())
                .setView(dialogView)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    val intent = Intent(context, Example1Activity::class.java)
                    val extras = mutableListOf(inputTitleView.text.toString())
                    if (inputSubtitleView.text.isNotBlank()) {
                        extras += inputSubtitleView.text.toString()
                    }
                    intent.putExtras(extrasOf<Example1Activity>(extras))
                    startActivity(intent)
                }
                .create()
        }
    }
}
