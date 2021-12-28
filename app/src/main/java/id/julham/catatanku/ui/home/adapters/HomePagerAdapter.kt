package id.julham.catatanku.ui.home.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import id.julham.catatanku.ui.home.fragments.AddNotesFragment
import id.julham.catatanku.ui.home.fragments.AllNotesFragment
import id.julham.catatanku.ui.home.fragments.SettingsFragment

internal class HomePagerAdapter(fragmentManager: FragmentManager) :
    FragmentPagerAdapter(
        fragmentManager,
        BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    )
{
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> AllNotesFragment()
            1 -> AddNotesFragment()
            2 -> SettingsFragment()
            else -> AddNotesFragment()
        }
    }

    override fun getCount() = 3
}