import { setFormVariant } from '@/store/formVariantSlice'
import { IoMdArrowRoundBack } from 'react-icons/io'
import { useDispatch } from 'react-redux'
import { Button } from './ui/button'

export function GoBackButton() {
	const dispatch = useDispatch()

	function handleVariantChange(variant: 'student' | 'teacher' | null) {
		dispatch(setFormVariant(variant))
		console.log(variant)
	}

	return (
		<Button
			className='border border-zinc-100 rounded-full'
			onClick={() => handleVariantChange(null)}
		>
			<IoMdArrowRoundBack />
		</Button>
	)
}
