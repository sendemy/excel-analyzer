import { Button } from '@/components/ui/button'
import {
	Form,
	FormControl,
	FormDescription,
	FormField,
	FormItem,
	FormLabel,
	FormMessage,
} from '@/components/ui/form'
import { Input } from '@/components/ui/input'
import { setGroupName } from '@/store/dataSlice'
import { zodResolver } from '@hookform/resolvers/zod'
import { motion } from 'framer-motion'
import { useState } from 'react'
import { useForm } from 'react-hook-form'
import { useDispatch } from 'react-redux'
import { z } from 'zod'
import { GoBackButton } from './GoBackButton'
import { ThreeDotsWave } from './ui/ThreeDotsWave'

const formSchema = z.object({
	group: z.string().optional(),
})

type FormSchema = z.infer<typeof formSchema>

export function FormComponent() {
	const [isLoading, setIsLoading] = useState(false)

	const dispatch = useDispatch()

	const form = useForm<FormSchema>({
		resolver: zodResolver(formSchema),
		defaultValues: {
			group: '',
		},
	})

	const variants = {
		initial: { y: 15, opacity: 0 },
		animate: { y: 0, opacity: 1 },
		exit: { y: -15, opacity: 0 },
	}

	async function onSubmit(value: z.infer<typeof formSchema>) {
		setIsLoading(true)

		setTimeout(() => {
			if (!value.group) return

			dispatch(setGroupName(value.group.replace(/-/, ' ')))
			setIsLoading(false)
			form.reset()
		}, 1000)
	}

	return (
		<>
			{isLoading ? (
				<>
					<motion.div
						variants={variants}
						initial='initial'
						animate='animate'
						exit='exit'
						transition={{ duration: 0.3, ease: 'linear' }}
						className='absolute top-4 left-4 z-10'
					>
						<GoBackButton setIsLoading={setIsLoading} />
					</motion.div>
					<motion.div
						variants={variants}
						initial='initial'
						animate='animate'
						exit='exit'
						transition={{ duration: 0.3, ease: 'linear' }}
						className='flex flex-col items-center gap-2 text-zinc-100'
					>
						<ThreeDotsWave />
						<p className='text-xl sm:text-3xl font-semibold'>
							Подождите, пока расписание сгенерируется
						</p>
					</motion.div>
				</>
			) : (
				<motion.div
					variants={variants}
					initial='initial'
					animate='animate'
					exit='exit'
					transition={{ duration: 0.3, ease: 'linear' }}
				>
					<Form {...form}>
						<form
							className='flex flex-col items-center gap-6 text-zinc-100'
							onSubmit={form.handleSubmit(onSubmit)}
						>
							<FormField
								control={form.control}
								name={'group'}
								render={({ field }) => (
									<FormItem className='w-48 sm:w-64 lg:w-80'>
										<FormLabel>Группа</FormLabel>
										<FormControl>
											<Input
												placeholder='напр. 23-ИСТ-4-2'
												required
												{...field}
											/>
										</FormControl>
										<FormDescription>Введите вашу группу</FormDescription>
										<FormMessage />
									</FormItem>
								)}
							/>
							<div className='flex items-center justify-center'>
								<Button
									variant={'secondary'}
									className='text-[0.8rem] sm:text-base px-4'
								>
									Создать расписание
								</Button>
							</div>
						</form>
					</Form>
				</motion.div>
			)}
		</>
	)
}
