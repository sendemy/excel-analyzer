import { createSlice, PayloadAction } from '@reduxjs/toolkit'

interface FormVariantState {
	formVariant: 'student' | 'teacher' | null
}

const initialState: FormVariantState = {
	formVariant: null,
}

const formVariantSlice = createSlice({
	name: 'formVariant',
	initialState,
	reducers: {
		setFormVariant(state, action: PayloadAction<'student' | 'teacher' | null>) {
			state.formVariant = action.payload
		},
	},
})

export const { setFormVariant } = formVariantSlice.actions

export default formVariantSlice.reducer
