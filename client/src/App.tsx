import { AnimatePresence } from 'framer-motion'
import { FormContainer } from './components/FormContainer'
import { ResultContainer } from './components/ResultContainer'

function App() {
	return (
		<main className='flex flex-col sm:flex-row h-[100vh] bg-zinc-50 overflow-hidden'>
			<AnimatePresence mode='wait'>
				<FormContainer />
			</AnimatePresence>

			<ResultContainer />
		</main>
	)
}

export default App
