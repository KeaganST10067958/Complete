package com.keagan.complete.ui.today

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.keagan.complete.R
import com.keagan.complete.core.StreakPrefs
import com.keagan.complete.databinding.FragmentTodayBinding
import java.util.Calendar

class TodayFragment : Fragment(R.layout.fragment_today) {

    private var _binding: FragmentTodayBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentTodayBinding.bind(view)

        // Quote placeholder (author kept, no refresh button)
        binding.tvQuote.text = getString(R.string.placeholder_quote_text)
        binding.tvAuthor.text = getString(R.string.placeholder_quote_author)

        val prefs = requireContext()
            .getSharedPreferences(StreakPrefs.FILE, Context.MODE_PRIVATE)

        val lastCheckIn = prefs.getLong(StreakPrefs.KEY_LAST_CHECKIN, 0L)
        val currentStreak = prefs.getInt(StreakPrefs.KEY_STREAK, 0)
        val checkedToday = isToday(lastCheckIn)

        binding.tvStreakCount.text = currentStreak.toString()
        updateCheckinButton(checkedToday)

        binding.btnCheckIn.setOnClickListener {
            val last = prefs.getLong(StreakPrefs.KEY_LAST_CHECKIN, 0L)
            val wasToday = isToday(last)

            if (!wasToday) {
                val wasYesterday = isYesterday(last)
                val newStreak = if (wasYesterday) currentStreak + 1 else 1

                prefs.edit()
                    .putLong(StreakPrefs.KEY_LAST_CHECKIN, System.currentTimeMillis())
                    .putInt(StreakPrefs.KEY_STREAK, newStreak)
                    .putBoolean(StreakPrefs.KEY_CHECKED_TODAY, true)
                    .apply()

                binding.tvStreakCount.text = newStreak.toString()
                updateCheckinButton(true)
            }
        }
    }

    private fun updateCheckinButton(checkedToday: Boolean) {
        if (checkedToday) {
            binding.btnCheckIn.isEnabled = false
            binding.btnCheckIn.text = getString(R.string.checked_in_today)
        } else {
            binding.btnCheckIn.isEnabled = true
            binding.btnCheckIn.text = getString(R.string.check_in_for_today)
        }
    }

    private fun isToday(timestamp: Long): Boolean {
        if (timestamp == 0L) return false
        val cal = Calendar.getInstance().apply { timeInMillis = timestamp }
        val now = Calendar.getInstance()
        return cal.get(Calendar.YEAR) == now.get(Calendar.YEAR) &&
                cal.get(Calendar.DAY_OF_YEAR) == now.get(Calendar.DAY_OF_YEAR)
    }

    private fun isYesterday(timestamp: Long): Boolean {
        if (timestamp == 0L) return false
        val cal = Calendar.getInstance().apply { timeInMillis = timestamp }
        val now = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }
        return cal.get(Calendar.YEAR) == now.get(Calendar.YEAR) &&
                cal.get(Calendar.DAY_OF_YEAR) == now.get(Calendar.DAY_OF_YEAR)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
