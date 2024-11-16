import { setFormVariant } from '@/store/formVariantSlice'
import { motion } from 'framer-motion'
import { useDispatch } from 'react-redux'
import { Button } from './ui/button'

export function Indentity() {
	const dispatch = useDispatch()

	function handleVariantChange(variant: 'student' | 'teacher' | null) {
		dispatch(setFormVariant(variant))
	}

	const variants = {
		initial: { y: 15, opacity: 0 },
		animate: { y: 0, opacity: 1 },
		exit: { y: -15, opacity: 0 },
	}

	return (
		<div className='bg-zinc-900 w-full sm:w-[50vw] h-[50vh] sm:h-full flex flex-col justify-center items-center gap-8'>
			<motion.div
				variants={variants}
				initial='initial'
				animate='animate'
				exit='exit'
				transition={{ duration: 0.3, ease: 'linear' }}
				className='bg-zinc-900 w-full sm:w-[50vw] h-[50vh] sm:h-full flex flex-col justify-center items-center gap-8'
			>
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
			</motion.div>
		</div>
	)
}
