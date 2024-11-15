import { RootState } from '@/store/store'
import { useSelector } from 'react-redux'
import { FormComponent } from './FormComponent'
import { GoBackButton } from './GoBackButton'

export function FormContainer() {
	const formVariant = useSelector(
		(state: RootState) => state.formVariant.formVariant
	)

	return (
		<div className='relative bg-zinc-900 w-full sm:w-[50vw] h-[50vh] sm:h-full flex justify-center items-center'>
			<div className='absolute top-4 left-4 z-10'>
				<GoBackButton />
			</div>

			{formVariant && <FormComponent />}
		</div>
	)
}
