package vcmsa.projects.prog7313_poe.core.extensions

import android.text.TextWatcher
import android.widget.EditText

// <editor-fold desc="Extensions">


/**
 * Hijack the onTextChanged event from an EditText.
 *
 * @author ST10257002
 */
fun EditText.onTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {

        /**
         * Called before the text is changed.
         * 
         * The method body of this function has been intentionally left blank as
         * it is not needed and may reduce performance with unnecessary 
         * implementations.
         */
        override fun beforeTextChanged(
            s: CharSequence?, start: Int, count: Int, after: Int
        ) {
        }


        /**
         * Called while the text is being changed.
         * 
         * The method body of this function has been intentionally left blank as
         * it is not needed and may reduce performance with unnecessary
         * implementations.
         */
        override fun onTextChanged(
            s: CharSequence?, start: Int, before: Int, count: Int
        ) {
        }


        /**
         * Called after the text has been changed.
         */
        override fun afterTextChanged(editable: android.text.Editable?) {
            afterTextChanged.invoke(editable.toString())
        }
    })
}


// </editor-fold>