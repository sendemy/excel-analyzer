import { IoMdArrowRoundBack } from 'react-icons/io'
import { Button } from './ui/button'

export function GoBackButton({
	setIsLoading,
}: {
	setIsLoading: (value: boolean) => void
}) {
	function goBack() {
		setIsLoading(false)
	}

	return (
		<Button
			className='border border-zinc-100 rounded-full'
			onClick={() => goBack()}
		>
			<IoMdArrowRoundBack />
		</Button>
	)
}
