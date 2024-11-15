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
import { useEffect } from 'react'
import { useForm } from 'react-hook-form'
import { useSelector } from 'react-redux'
import { z } from 'zod'

const formSchema = z.object({
	name: z.string().optional(),
	group: z.string().optional(),
})

type FormSchema = z.infer<typeof formSchema>

export function FormComponent() {
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

	async function onSubmit(values: z.infer<typeof formSchema>) {
		console.log(values)
	}

	useEffect(() => {
		form.reset(formVariant === 'student' ? { group: '' } : { name: '' })
	}, [formVariant, form.reset])

	return (
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
									: 'Введите ваше ФИО'}
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
	)
}
