import { createSlice, PayloadAction } from '@reduxjs/toolkit'

interface IGroupNameState {
	groupName: string | null
}

const initialState: IGroupNameState = {
	groupName: null,
}

const groupNameSlice = createSlice({
	name: 'groupName',
	initialState,
	reducers: {
		setGroupName(state, action: PayloadAction<string | null>) {
			state.groupName = action.payload
		},
	},
})

export const { setGroupName } = groupNameSlice.actions

export default groupNameSlice.reducer
