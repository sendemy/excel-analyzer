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
import { RootState } from '@/store/store'
import { zodResolver } from '@hookform/resolvers/zod'
import { motion } from 'framer-motion'
import { useEffect, useState } from 'react'
import { useForm } from 'react-hook-form'
import { useSelector } from 'react-redux'
import { z } from 'zod'
import { ThreeDotsWave } from './ui/ThreeDotsWave'

const formSchema = z.object({
	name: z.string().optional(),
	group: z.string().optional(),
})

type FormSchema = z.infer<typeof formSchema>

export function FormComponent() {
	const [isLoading, setIsLoading] = useState(false)

	const formVariant = useSelector(
		(state: RootState) => state.formVariant.formVariant
	)

	const form = useForm<FormSchema>({
		resolver: zodResolver(formSchema),
		defaultValues: {
			group: '',
			name: '',
		},
	})

	const variants = {
		initial: { y: 15, opacity: 0 },
		animate: { y: 0, opacity: 1 },
		exit: { y: -15, opacity: 0 },
	}

	async function onSubmit(values: z.infer<typeof formSchema>) {
		setIsLoading(true)
		console.log(values)
	}

	useEffect(() => {
		form.reset(formVariant === 'student' ? { group: '' } : { name: '' })
	}, [formVariant, form.reset])

	return (
		<>
			{isLoading ? (
				<motion.div
					key='identity'
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
			) : (
				<Form {...form}>
					<form
						className='flex flex-col items-center gap-6 text-zinc-100'
						onSubmit={form.handleSubmit(onSubmit)}
					>
						<FormField
							control={form.control}
							name={formVariant === 'student' ? 'group' : 'name'}
							render={({ field }) => (
								<FormItem className='w-48 sm:w-64 lg:w-80'>
									<FormLabel>
										{formVariant === 'student' ? 'Группа' : 'Имя'}
									</FormLabel>
									<FormControl>
										<Input
											placeholder={
												formVariant === 'student'
													? 'напр. 23-ИСТ-4-2'
													: 'напр. Иванов И. И.'
											}
											required
											{...field}
										/>
									</FormControl>
									<FormDescription>
										{formVariant === 'student'
											? 'Введите вашу группу'
											: 'Введите ваше имя в формате Фамилия И. О.'}
									</FormDescription>
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
			)}
		</>
	)
}
