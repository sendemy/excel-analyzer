import { motion } from 'framer-motion'
import { FormComponent } from './FormComponent'

export function FormContainer() {
	const variants = {
		initial: { y: 15, opacity: 0 },
		animate: { y: 0, opacity: 1 },
		exit: { y: -15, opacity: 0 },
	}

	return (
		<div className='relative bg-zinc-900 w-full sm:w-[50vw] h-[50vh] sm:h-full flex justify-center items-center'>
			<motion.div
				variants={variants}
				initial='initial'
				animate='animate'
				exit='exit'
				transition={{ duration: 0.3, ease: 'linear' }}
				className='relative bg-zinc-900 w-full sm:w-[50vw] h-[50vh] sm:h-full flex justify-center items-center'
			>
				<FormComponent />
			</motion.div>
		</div>
	)
}
