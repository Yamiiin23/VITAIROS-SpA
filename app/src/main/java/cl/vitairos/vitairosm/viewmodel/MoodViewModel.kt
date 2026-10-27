package cl.vitairos.vitairosm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.vitairos.vitairosm.data.local.MoodEntry
import cl.vitairos.vitairosm.data.repository.VitairosRepository
import cl.vitairos.vitairosm.util.Validators
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MoodFormState(
    val mood: Int? = null,
    val domain: String? = null,
    val note: String = "",
    val moodError: String? = null,
    val domainError: String? = null,
    val noteError: String? = null
)

class MoodViewModel(private val repo: VitairosRepository) : ViewModel() {
    val entries: StateFlow<List<MoodEntry>> =
        repo.moods.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _form = MutableStateFlow(MoodFormState())
    val form: StateFlow<MoodFormState> = _form.asStateFlow()

    fun onMood(v: Int) = _form.update { it.copy(mood = v, moodError = null) }
    fun onDomain(v: String) = _form.update { it.copy(domain = v, domainError = null) }
    fun onNote(v: String) = _form.update {
        val text = v.take(Validators.NOTE_MAX)
        it.copy(note = text, noteError = Validators.note(text))
    }

    fun resetForm() { _form.value = MoodFormState() }

    fun save(onSaved: () -> Unit) {
        val s = _form.value
        val checked = s.copy(
            moodError = Validators.mood(s.mood),
            domainError = Validators.domain(s.domain),
            noteError = Validators.note(s.note)
        )
        _form.value = checked
        if (checked.moodError != null || checked.domainError != null || checked.noteError != null) return

        viewModelScope.launch {
            repo.addMood(MoodEntry(mood = s.mood!!, domain = s.domain!!, note = s.note.trim(), createdAt = System.currentTimeMillis()))
            _form.value = MoodFormState()
            onSaved()
        }
    }

    fun delete(entry: MoodEntry) = viewModelScope.launch { repo.deleteMood(entry) }
}
