import { configureStore } from '@reduxjs/toolkit'
import formVariantReducer from './formVariantSlice'

export const store = configureStore({
	reducer: {
		formVariant: formVariantReducer,
	},
})

export type RootState = ReturnType<typeof store.getState>
export type AppDispatch = typeof store.dispatch
