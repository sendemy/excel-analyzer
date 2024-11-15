import { setFormVariant } from '@/store/formVariantSlice'
import { useDispatch } from 'react-redux'
import { Button } from './ui/button'

export function Indentity() {
	const dispatch = useDispatch()

	function handleVariantChange(variant: 'student' | 'teacher' | null) {
		dispatch(setFormVariant(variant))
	}

	return (
		<div className='bg-zinc-900 w-full sm:w-[50vw] h-[50vh] sm:h-full flex flex-col justify-center items-center gap-8'>
			<h2 className='text-5xl font-semibold text-zinc-100'>Кто вы?</h2>
			<div className='flex gap-8'>
				<Button
					title='Student'
					variant={'secondary'}
					onClick={() => handleVariantChange('student')}
				>
					Студент
				</Button>
				<Button
					title='Teacher'
					variant={'secondary'}
					onClick={() => handleVariantChange('teacher')}
				>
					Преподаватель
				</Button>
			</div>
		</div>
	)
}
