<template>
	<view class="page">
		<view class="header">
			<text>Welcome to ScootGo</text>
		</view>
		<view class="container">
			<view class="title">
				<text>Register</text>
			</view>
			<form>
				<view class="form-group">
					<label>Username</label>
					<input v-model="form.username" type="text" aria-label="username" placeholder="Enter username"
						:placeholder-style="placeholderStyle" class="input-field" />
					<span v-if="errors.username" class="error-message">{{ errors.username }}</span>
				</view>

				<view class="form-group">
					<label>Password</label>
					<input v-model="form.password" type="password" aria-label="password" placeholder="Enter password"
						:placeholder-style="placeholderStyle" class="input-field" />
					<span v-if="errors.password" class="error-message">{{ errors.password }}</span>
				</view>

				<view class="form-group">
					<label>Email</label>
					<input v-model="form.email" type="email" aria-label="email" placeholder="Enter email"
						:placeholder-style="placeholderStyle" class="input-field" />
					<span v-if="errors.email" class="error-message">{{ errors.email }}</span>
				</view>

				<view class="form-group">
					<label>Mobile</label>
					<input v-model="form.mobile" type="text" aria-label="mobile" placeholder="Enter mobile number"
						:placeholder-style="placeholderStyle" class="input-field" />
					<span v-if="errors.mobile" class="error-message">{{ errors.mobile }}</span>
				</view>

				<view class="form-group">
					<label>Card</label>
					<input v-model="form.bankCard" type="text" aria-label="bankCard" placeholder="Enter Card number"
						:placeholder-style="placeholderStyle" class="input-field" />
					<span v-if="errors.bankCard" class="error-message">{{ errors.bankCard }}</span>
				</view>

				<view class="form-group">
					<label>Birthday</label>
					<view class="example-body">
						<uni-datetime-picker type="date" :clear-icon="false" v-model="form.birthday"
							@maskClick="maskClick" />
					</view>
					<span v-if="errors.birthday" class="error-message">{{ errors.birthday }}</span>
				</view>
			</form>
			<div class="form-group">
				<button @click="register" :disabled="loading" class="login-btn">
					{{ loading ? 'Registering...' : 'Register' }}
				</button>
			</div>
			<div class="form-group">
				<button @click="goToLogin" class="login-btn">
					Go to Login
				</button>
			</div>
		</view>
	</view>
</template>
<script>
	export default {
		name: 'UserRegister',
		data() {
			return {
				form: {
					username: '',
					password: '',
					email: '',
					mobile: '',
					bankCard: '',
					birthday: '',
					userType: 0, // default: normal user
				},
				loading: false,
				message: '',
				messageType: 'success',
				registeredUser: null,
				errors: {},
				placeholderStyle: 'color: #2c3e50; font-size: 14px;'
			};
		},
		methods: {
			maskClick() {
				console.log('----maskClick');
			},
			getDate(date, addZero = true) {
				date = new Date(date)
				const year = date.getFullYear()
				const month = date.getMonth() + 1
				const day = date.getDate()
				return `${year}-${addZero ? this.addZero(month) : month}-${addZero ? this.addZero(day) : day}`
			},
			getDateTime(date, addZero = true) {
				return `${this.getDate(date, addZero)} ${this.getTime(date, addZero)}`
			},
			getTime(date, addZero = true) {
				date = new Date(date)
				const hour = date.getHours()
				const minute = date.getMinutes()
				const second = date.getSeconds()
				return this.hideSecond ?
					`${addZero ? this.addZero(hour) : hour}:${addZero ? this.addZero(minute) : minute}` :
					`${addZero ? this.addZero(hour) : hour}:${addZero ? this.addZero(minute) : minute}:${addZero ? this.addZero(second) : second}`
			},
			addZero(num) {
				if (num < 10) {
					num = `0${num}`
				}
				return num
			},
			validate() {
				this.errors = {};
				// Username validation: 3-20 characters, letters, numbers, and underscores only
				const usernameRegex = /^[a-zA-Z0-9_]{3,20}$/;
				if (!this.form.username) {
					this.errors.username = 'Username is required';
				} else if (!usernameRegex.test(this.form.username)) {
					this.errors.username =
						'Username must be 3-20 characters and can only contain letters, numbers, and underscores';
				}

				// Password validation: at least 8 characters, containing uppercase, lowercase, and number
				const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{8,}$/;
				if (!this.form.password) {
					this.errors.password = 'Password is required';
				} else if (!passwordRegex.test(this.form.password)) {
					this.errors.password =
						'Password must be at least 8 characters and include at least one uppercase letter, one lowercase letter, and one number';
				}

				// Email validation: valid email format
				const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
				if (!this.form.email) {
					this.errors.email = 'Email is required';
				} else if (!emailRegex.test(this.form.email)) {
					this.errors.email = 'Please enter a valid email address';
				}

				// Mobile number validation: 10-13 digits
				const mobileRegex = /^[0-9]{10,13}$/;
				if (!this.form.mobile) {
					this.errors.mobile = 'Mobile number is required';
				} else if (!mobileRegex.test(this.form.mobile)) {
					this.errors.mobile = 'Mobile number must be between 10-13 digits';
				}

				const cardRegex = /^[0-9]{13,16}$/;
				if (!this.form.bankCard) {
					this.errors.bankCard = 'Card number is required';
				} else if (!cardRegex.test(this.form.bankCard)) {
					this.errors.bankCard = 'Card number must be between 13-16 digits';
				}


				// Birthday validation: valid date format and must be in the past
				const today = new Date();
				const birthdayDate = new Date(this.form.birthday);
				if (!this.form.birthday) {
					this.errors.birthday = 'Birthday is required';
				} else if (birthdayDate >= today) {
					this.errors.birthday = 'Birthday must be in the past';
				}

				return Object.keys(this.errors).length === 0;
			},
			async register() {
				this.loading = true;
				this.message = '';
				this.registeredUser = null;

				if (!this.validate()) {
					this.message = 'Please correct the errors below';
					this.messageType = 'error';
					this.loading = false;
					return;
				}

				try {
					const [err, res] = await uni.request({
						url: `${this.$baseURL}/api/auth/register`,
						method: 'POST',
						data: this.form,
						header: {
							'Content-Type': 'application/json'
						}
					}).then(res => [null, res]).catch(err => [err, null]);

					if (err || res.statusCode !== 200) {
						let errorMessage = 'Registration failed';
						if (res?.data) {
							if (typeof res.data === 'string') {
								errorMessage = res.data;
							} else if (typeof res.data === 'object' && res.data.message) {
								errorMessage = res.data.message;
							}
						} else if (err) {
							errorMessage = err.message || 'Network error, please try again';
						}

						this.message = errorMessage;
						this.messageType = 'error';

						uni.showToast({
							title: this.message,
							icon: 'none',
							duration: 2000
						});

						throw new Error(errorMessage);
					}

					uni.showToast({
						title: 'Registration successful!',
						icon: 'none',
						duration: 2000
					});

					this.form = {
						username: '',
						password: '',
						email: '',
						mobile: '',
						bankCard: '',
						birthday: '',
						userType: 0,
					};

					setTimeout(() => {
						this.goToLogin();
					}, 2000);

				} catch (error) {
					console.error('Registration error:', error);
				} finally {
					this.loading = false;
				}
			},

			goToLogin() {
				uni.navigateTo({
					url: '/pages/UserLogin/UserLogin'
				});
			}
		}
	}
</script>

<style scoped lang="scss">
	@import '../index.scss';
</style>
<style scoped lang="scss">
	.error-message {
		color: red;
	}
	
	.picker-box {
		width: 90%;
		height: 80rpx;
		padding: 0 20rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		background-color: #f7f7f7;
		border: 1rpx solid #ddd;
		border-radius: 8rpx;
		margin: 10rpx;
	}

	.picker-text {
		font-size: 30rpx;
		color: #666;
		flex: 1;
	}

	.picker-box:hover {
		border-color: #aaa;
	}
</style>