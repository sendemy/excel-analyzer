import { AnimatePresence } from 'framer-motion'
import { useSelector } from 'react-redux'
import { FormContainer } from './components/FormContainer'
import { Indentity } from './components/Indentity'
import { ResultContainer } from './components/ResultContainer'
import { RootState } from './store/store'

function App() {
	const formVariant = useSelector(
		(state: RootState) => state.formVariant.formVariant
	)

	return (
		<main className='flex flex-col sm:flex-row h-[100vh] bg-zinc-50 overflow-hidden'>
			<AnimatePresence mode='wait'>
				{formVariant ? <FormContainer /> : <Indentity />}
			</AnimatePresence>

			<ResultContainer />
		</main>
	)
}

export default App
