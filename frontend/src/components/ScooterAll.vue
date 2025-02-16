<template>
  <div>
    <h2>All Scooters</h2>
    <table>
      <thead>
      <tr>
        <th>ID</th>
        <th>Location</th>
        <th>Price per Hour</th>
        <th>Price per Four Hours</th>
        <th>Price per Day</th>
        <th>Price per Week</th>
        <th></th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="scooter in scooters" :key="scooter.id">
        <td>{{ scooter.id }}</td>
        <td>{{ scooter.location }}</td>
        <td>{{ scooter.priceHour }}</td>
        <td>{{ scooter.priceFourHour }}</td>
        <td>{{ scooter.priceDay }}</td>
        <td>{{ scooter.priceWeek }}</td>
        <td><button @click="book">Book</button></td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'ScooterAll',
  data() {
    return {
      scooters: []
    };
  },
  mounted() {
    this.fetchScooters();
  },
  methods: {
    async fetchScooters() {
      try {
        const response = await axios.get('http://localhost:8080/api/scooters/getAll');
        this.scooters = response.data;
      } catch (error) {
        console.error('Error fetching scooters:', error);
      }
    }
  }
};
</script>