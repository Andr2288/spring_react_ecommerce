import {create} from "zustand"
import {axiosInstance} from "../lib/axios.js";
import toast from "react-hot-toast";

export const useAuthStore = create((set, get) => ({
    authUser: null,
    isSigningUp: false,
    isLoggingIn: false,
    isUpdatingProfile: false,
    isCheckingAuth: true,

    checkAuth: async () => {
        try {
            const token = localStorage.getItem("token");
            if (!token) {
                set({authUser: null, isCheckingAuth: false});
                return;
            }

            const res = await axiosInstance.get("/auth/check");
            set({authUser: res.data, isCheckingAuth: false});
        }
        catch (error) {
            console.log("Error in checkAuth", error);
            localStorage.removeItem("token");
            set({authUser: null, isCheckingAuth: false});
        }
    },

    signup: async (userData) => {
        set({isSigningUp: true});
        try {
            const res = await axiosInstance.post("/auth/register", userData);

            // Store JWT token
            localStorage.setItem("token", res.data.token);

            // Set user data
            set({
                authUser: {
                    name: res.data.name,
                    email: res.data.email,
                    phone: res.data.phone,
                    isAdmin: res.data.isAdmin || false
                },
                isSigningUp: false
            });

            toast.success("Account created successfully!");
        } catch (error) {
            set({isSigningUp: false});
            const errorMessage = error.response?.data?.message || "Registration failed";
            toast.error(errorMessage);
            throw new Error(errorMessage);
        }
    },

    login: async (credentials) => {
        set({isLoggingIn: true});
        try {
            const res = await axiosInstance.post("/auth/login", credentials);

            // Store JWT token
            localStorage.setItem("token", res.data.token);

            // Set user data
            set({
                authUser: {
                    name: res.data.name,
                    email: res.data.email,
                    phone: res.data.phone,
                    isAdmin: res.data.isAdmin || false
                },
                isLoggingIn: false
            });

            toast.success(`Welcome back, ${res.data.name}!`);
        } catch (error) {
            set({isLoggingIn: false});
            const errorMessage = error.response?.data?.message || "Login failed";
            toast.error(errorMessage);
            throw new Error(errorMessage);
        }
    },

    logout: () => {
        localStorage.removeItem("token");
        set({authUser: null});
        toast.success("Logged out successfully");
    },

    updateProfile: async (userData) => {
        set({isUpdatingProfile: true});
        try {
            const res = await axiosInstance.put("/profile", userData);

            set({
                authUser: {
                    ...get().authUser,
                    ...res.data
                },
                isUpdatingProfile: false
            });

            toast.success("Profile updated successfully!");
        } catch (error) {
            set({isUpdatingProfile: false});
            const errorMessage = error.response?.data?.message || "Profile update failed";
            toast.error(errorMessage);
            throw new Error(errorMessage);
        }
    }
}));