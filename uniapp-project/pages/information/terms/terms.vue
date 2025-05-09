<template>
	<view class="terms-container">
		<text class="title">ScootGo Terms and Conditions</text>
		<scroll-view scroll-y style="height: 90vh;">
			<rich-text :nodes="termsContent" class="terms-content" />
		</scroll-view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				termsContent: `
		        <div>
		          <p>Welcome to ScootGo. These terms and conditions outline the rules and regulations for the use of the ScootGo application and services.</p>
		          <p>By accessing this application, we assume you accept these terms and conditions. Do not continue to use ScootGo if you do not agree to all of the terms and conditions stated on this page.</p>
		          <p><b>1. User Eligibility</b><br />
		          To use our services, you must be at least 16 years of age and possess a valid government-issued identification. Users under 18 must have parental or legal guardian consent.</p>
		          <p><b>2. Account Responsibility</b><br />
		          You are responsible for maintaining the confidentiality of your account credentials and are fully responsible for all activities that occur under your account.</p>
		          <ul>
		            <li>Scooters must be used responsibly and parked legally.</li>
		            <li>Helmets are recommended during all rides.</li>
		            <li>You must not ride under the influence of drugs or alcohol.</li>
		            <li>Any damage caused by reckless use will be your responsibility.</li>
		          </ul>
		          <p><b>4. Payment and Fees</b><br />
		          Usage fees are charged based on time and distance. Additional charges may apply for parking violations, late returns (including overtime returns), or damages. A penalty fee will be imposed for returning the scooter beyond the allowed time limit. All fees will be displayed before ride confirmation.</p>
		          <p><b>5. Cancellations and Refunds</b><br />
				  <ul>
					<li>Rides can be cancelled before paid.</li>
					<li>Once the ride has started, no refunds are available unless due to system failure.</li>
				  </ul>
		          <p><b>6. Data Privacy</b><br />
		          We are committed to protecting your privacy. Your personal information is only used to improve our service and will not be shared with third parties without consent. Please refer to our Privacy Policy for more details.</p>
		          <p><b>7. Termination of Use</b><br />
		          ScootGo reserves the right to suspend or terminate your account if any suspicious, illegal, or abusive behavior is detected.</p>
		          <p><b>8. Changes to Terms</b><br />
		          We may update these terms from time to time. Continued use of the app after any changes constitutes acceptance of the new terms.</p>
		          <p>If you have any questions or concerns regarding these Terms, please contact us at <a href="mailto:support@scootgo.com">support@scootgo.com</a>.</p>
		          <p>Thank you for choosing ScootGo!</p>
		        </div>
		      `
			}
		},
		onLoad() {
			uni.setNavigationBarTitle({
				title: 'Terms of Service'
			});
		}
	};
</script>

<style>
	.terms-container {
		padding: 30rpx;
	}

	.title {
		font-size: 36rpx;
		font-weight: bold;
		margin-bottom: 30rpx;
	}

	.terms-content {
		font-size: 28rpx;
		color: #333;
		line-height: 1.8;
		padding: 20rpx;
	}
</style>