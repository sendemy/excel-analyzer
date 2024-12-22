import { RootState } from '@/store/store'
import { useEffect, useState } from 'react'
import { useSelector } from 'react-redux'
import { Button } from './ui/button'

export function ResultContainer() {
	const [isSuccessfulFetch, setIsSuccessfulFetch] = useState<boolean>(false)

	const groupName = useSelector((state: RootState) => state.data.groupName)

	useEffect(() => {
		if (groupName) {
			fetch(`http://localhost:8080/generate/${groupName}`)
				.then((res) => {
					if (!res.ok) {
						throw new Error(`HTTP error! Status: ${res.status}`)
					}

					setIsSuccessfulFetch(true)
				})
				.catch((error) => {
					console.log(`Error occured: ${error.message}`)
					setIsSuccessfulFetch(false)
				})
		}
	}, [groupName])

	return (
		<>
			<div className='w-full sm:w-[50vw] h-[50vh] sm:h-full flex flex-col justify-center items-center text-center bg-zinc-100'>
				<h2 className='text-xl sm:text-3xl font-semibold text-zinc-900'>
					{groupName && isSuccessfulFetch && (
						<Button>
							<a
								href={`http://localhost:8080/generate/${groupName}`}
								download='spreadsheet.xlsx'
							>
								Загрузить файл
							</a>
						</Button>
					)}
					{!groupName && !isSuccessfulFetch && (
						<p>Расписание появится здесь после генерации.</p>
					)}
					{groupName && !isSuccessfulFetch && (
						<p>
							Произошла ошибка. Пожалуйста, проверьте правильность ввода группы.
						</p>
					)}
				</h2>
			</div>
		</>
	)
}
