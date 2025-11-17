import { Link, useLocation } from "react-router-dom";
import { useAuthStore } from "../store/useAuthStore.js";
import { LogOut, ShoppingBag, User } from "lucide-react";

const Navbar = () => {
    const { authUser, logout } = useAuthStore();
    const location = useLocation();

    const handleLogout = () => {
        logout();
    };

    return (
        <nav className="bg-white shadow-lg border-b border-gray-200">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                <div className="flex justify-between h-16">
                    <div className="flex items-center">
                        <Link to={authUser ? "/home" : "/"} className="flex items-center space-x-2">
                            <ShoppingBag className="h-8 w-8 text-blue-600" />
                            <span className="text-xl font-bold text-gray-900">E-Store</span>
                        </Link>
                    </div>

                    <div className="flex items-center space-x-4">
                        {authUser ? (
                            // Authenticated user menu
                            <>
                                <div className="flex items-center space-x-2 text-gray-700">
                                    <User className="h-5 w-5" />
                                    <span className="text-sm font-medium">{authUser.name}</span>
                                    {authUser.isAdmin && (
                                        <span className="px-2 py-1 bg-blue-100 text-blue-800 text-xs font-medium rounded-full">
                                            Admin
                                        </span>
                                    )}
                                </div>

                                <button
                                    onClick={handleLogout}
                                    className="inline-flex items-center space-x-1 text-gray-500 hover:text-gray-700 px-3 py-2 rounded-md text-sm font-medium transition-colors"
                                >
                                    <LogOut className="h-4 w-4" />
                                    <span>Logout</span>
                                </button>
                            </>
                        ) : (
                            // Guest user menu
                            <div className="flex items-center space-x-4">
                                <Link
                                    to="/login"
                                    className={`px-3 py-2 rounded-md text-sm font-medium transition-colors ${
                                        location.pathname === "/login"
                                            ? "bg-blue-100 text-blue-700"
                                            : "text-gray-500 hover:text-gray-700"
                                    }`}
                                >
                                    Login
                                </Link>
                                <Link
                                    to="/register"
                                    className={`px-3 py-2 rounded-md text-sm font-medium transition-colors ${
                                        location.pathname === "/register"
                                            ? "bg-blue-100 text-blue-700"
                                            : "text-gray-500 hover:text-gray-700"
                                    }`}
                                >
                                    Register
                                </Link>
                            </div>
                        )}
                    </div>
                </div>
            </div>
        </nav>
    );
};

export default Navbar;