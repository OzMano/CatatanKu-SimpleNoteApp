package id.julham.catatanku.utils

import id.julham.catatanku.R

class NoteColorPicker {
    companion object {
        fun noteColor(id: Int): Int {

            var color = R.id.seven
            when (id) {
                R.id.one -> {
                    color = R.color.one
                }
                R.id.two -> {
                    color = R.color.two
                }
                R.id.three -> {
                    color = R.color.three
                }
                R.id.four -> {
                    color = R.color.four
                }
                R.id.five -> {
                    color = R.color.five
                }
                R.id.six -> {
                    color = R.color.six
                }
                R.id.seven -> {
                    color = R.color.seven
                }
                R.id.eight -> {
                    color = R.color.eight
                }
            }
            return color
        }
    }
}