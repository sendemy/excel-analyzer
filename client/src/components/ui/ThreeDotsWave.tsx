import { motion } from 'framer-motion'

const loadingContainer = {
	width: '2rem',
	height: '2rem',
	display: 'flex',
	justifyContent: 'space-around',
}

const loadingCircle = {
	display: 'block',
	width: '0.5rem',
	height: '0.5rem',
	backgroundColor: 'rgb(244 244 245)',
	borderRadius: '0.25rem',
}

const loadingContainerVariants = {
	start: {
		transition: {
			staggerChildren: 0.1,
		},
	},
	end: {
		transition: {
			staggerChildren: 0.1,
		},
	},
}

const loadingCircleVariants = {
	start: {
		y: '0%',
	},
	end: {
		y: '100%',
	},
}

const loadingCircleTransition = {
	duration: 0.4,
	repeat: Infinity,
	repeatType: 'mirror' as 'mirror' | 'loop' | 'reverse' | undefined,
	ease: 'easeInOut',
}

export const ThreeDotsWave = () => {
	return (
		<motion.span
			style={loadingContainer}
			variants={loadingContainerVariants}
			initial='start'
			animate='end'
		>
			<motion.span
				style={loadingCircle}
				variants={loadingCircleVariants}
				transition={loadingCircleTransition}
			/>
			<motion.span
				style={loadingCircle}
				variants={loadingCircleVariants}
				transition={loadingCircleTransition}
			/>
			<motion.span
				style={loadingCircle}
				variants={loadingCircleVariants}
				transition={loadingCircleTransition}
			/>
		</motion.span>
	)
}
